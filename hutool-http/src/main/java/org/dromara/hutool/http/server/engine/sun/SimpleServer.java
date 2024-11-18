/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.server.engine.sun;

import com.sun.net.httpserver.*;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.sun.filter.HttpFilter;
import org.dromara.hutool.http.server.engine.sun.filter.SimpleFilter;
import org.dromara.hutool.http.server.handler.RootHandler;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * 简易Http服务器，基于{@link HttpServer}
 *
 * @author looly
 * @since 5.2.5
 */
public class SimpleServer {

	private final SunHttpServerEngine engine;

	/**
	 * 构造
	 *
	 * @param port 监听端口
	 */
	public SimpleServer(final int port) {
		this(new InetSocketAddress(port));
	}

	/**
	 * 构造
	 *
	 * @param hostname 监听地址
	 * @param port     监听端口
	 */
	public SimpleServer(final String hostname, final int port) {
		this(new InetSocketAddress(hostname, port));
	}

	/**
	 * 构造
	 *
	 * @param address 监听地址
	 */
	public SimpleServer(final InetSocketAddress address) {
		this(address, null);
	}

	/**
	 * 构造
	 *
	 * @param address    监听地址
	 * @param sslContext ssl配置
	 */
	public SimpleServer(final InetSocketAddress address, final SSLContext sslContext) {
		this.engine = new SunHttpServerEngine();

		final ServerConfig serverConfig = ServerConfig.of()
			.setHost(address.getHostName())
			.setPort(address.getPort())
			.setSslContext(sslContext);
		this.engine.init(serverConfig);
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #setRoot(File)}  </li>
	 *     <li>{@link #setRoot(String)}  </li>
	 *     <li>{@link #createContext(String, HttpHandler)} </li>
	 *     <li>{@link #addHandler(String, HttpHandler)}</li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SimpleServer addFilter(final Filter filter) {
		this.engine.addFilter(filter);
		return this;
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #setRoot(File)}  </li>
	 *     <li>{@link #setRoot(String)}  </li>
	 *     <li>{@link #createContext(String, HttpHandler)} </li>
	 *     <li>{@link #addHandler(String, HttpHandler)}</li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SimpleServer addFilter(final HttpFilter filter) {
		return addFilter(new SimpleFilter() {
			@Override
			public void doFilter(final HttpExchange httpExchange, final Chain chain) throws IOException {
				final HttpExchangeWrapper httpExchangeWrapper = new HttpExchangeWrapper(httpExchange);
				filter.doFilter(httpExchangeWrapper.getRequest(), httpExchangeWrapper.getResponse(), chain);
			}
		});
	}

	/**
	 * 增加请求处理规则
	 *
	 * @param path    路径，例如:/a/b 或者 a/b
	 * @param handler 处理器，包括请求和响应处理
	 * @return this
	 * @see #createContext(String, HttpHandler)
	 */
	public SimpleServer addHandler(final String path, final HttpHandler handler) {
		createContext(path, handler);
		return this;
	}

	/**
	 * 创建请求映射上下文，创建后，用户访问指定路径可使用{@link HttpHandler} 中的规则进行处理
	 *
	 * @param path    路径，例如:/a/b 或者 a/b
	 * @param handler 处理器，包括请求和响应处理
	 * @return {@link HttpContext}
	 * @since 5.5.7
	 */
	public HttpContext createContext(String path, final HttpHandler handler) {
		// 非/开头的路径会报错
		path = StrUtil.addPrefixIfNot(path, StrUtil.SLASH);
		return this.engine.createContext(path, handler);
	}

	/**
	 * 增加请求处理规则，使用默认的{@link RootHandler}，默认从当前项目根目录读取页面
	 *
	 * @param path   路径，例如:/a/b 或者 a/b
	 * @param action 处理器，包括请求和响应处理
	 * @return this
	 * @since 6.0.0
	 */
	public SimpleServer addAction(final String path, final org.dromara.hutool.http.server.handler.HttpHandler action) {
		return addHandler(path, exchange -> {
			final HttpExchangeWrapper exchangeWrapper = new HttpExchangeWrapper(exchange);
			action.handle(exchangeWrapper.getRequest(), exchangeWrapper.getResponse());
		});
	}

	/**
	 * 设置根目录，默认的页面从root目录中读取解析返回
	 *
	 * @param root 路径
	 * @return this
	 */
	public SimpleServer setRoot(final String root) {
		return setRoot(new File(root));
	}

	/**
	 * 设置根目录，默认的页面从root目录中读取解析返回
	 *
	 * @param root 路径
	 * @return this
	 */
	public SimpleServer setRoot(final File root) {
		addAction("/", new RootHandler(root));
		return this;
	}

	/**
	 * 设置自定义线程池
	 *
	 * @param executor {@link Executor}
	 * @return this
	 */
	public SimpleServer setExecutor(final Executor executor) {
		this.engine.setExecutor(executor);
		return this;
	}

	/**
	 * 获得原始HttpServer对象
	 *
	 * @return {@link HttpServer}
	 */
	public HttpServer getRawServer() {
		return this.engine.getRawEngine();
	}

	/**
	 * 获取服务器地址信息
	 *
	 * @return {@link InetSocketAddress}
	 */
	public InetSocketAddress getAddress() {
		return getRawServer().getAddress();
	}

	/**
	 * 启动Http服务器，启动后会阻塞当前线程
	 */
	public void start() {
		final InetSocketAddress address = getAddress();
		Console.log("Hutool Simple Http Server listen on 【{}:{}】", address.getHostName(), address.getPort());
		this.engine.start();
	}
}
