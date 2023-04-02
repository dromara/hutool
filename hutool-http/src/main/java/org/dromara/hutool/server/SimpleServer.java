/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.server;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.server.handler.ActionHandler;
import org.dromara.hutool.thread.GlobalThreadPool;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.server.action.Action;
import org.dromara.hutool.server.action.RootAction;
import org.dromara.hutool.server.filter.HttpFilter;
import org.dromara.hutool.server.filter.SimpleFilter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 简易Http服务器，基于{@link HttpServer}
 *
 * @author looly
 * @since 5.2.5
 */
public class SimpleServer {

	private final HttpServer server;
	private final List<Filter> filters;

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
	 * @param address 监听地址
	 * @param configurator https配置信息，用于使用自定义SSL（TLS）证书等
	 */
	public SimpleServer(final InetSocketAddress address, final HttpsConfigurator configurator) {
		try {
			if(null != configurator){
				final HttpsServer server = HttpsServer.create(address, 0);
				server.setHttpsConfigurator(configurator);
				this.server = server;
			} else{
				this.server = HttpServer.create(address, 0);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		setExecutor(GlobalThreadPool.getExecutor());
		filters = new ArrayList<>();
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
	 *     <li>{@link #addAction(String, Action)} </li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SimpleServer addFilter(final Filter filter) {
		this.filters.add(filter);
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
	 *     <li>{@link #addAction(String, Action)} </li>
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
				filter.doFilter(new HttpServerRequest(httpExchange), new HttpServerResponse(httpExchange), chain);
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
		final HttpContext context = this.server.createContext(path, handler);
		// 增加整体过滤器
		context.getFilters().addAll(this.filters);
		return context;
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
		return addAction("/", new RootAction(root));
	}

	/**
	 * 增加请求处理规则
	 *
	 * @param path   路径
	 * @param action 处理器
	 * @return this
	 */
	public SimpleServer addAction(final String path, final Action action) {
		return addHandler(path, new ActionHandler(action));
	}

	/**
	 * 设置自定义线程池
	 *
	 * @param executor {@link Executor}
	 * @return this
	 */
	public SimpleServer setExecutor(final Executor executor) {
		this.server.setExecutor(executor);
		return this;
	}

	/**
	 * 获得原始HttpServer对象
	 *
	 * @return {@link HttpServer}
	 */
	public HttpServer getRawServer() {
		return this.server;
	}

	/**
	 * 获取服务器地址信息
	 *
	 * @return {@link InetSocketAddress}
	 */
	public InetSocketAddress getAddress() {
		return this.server.getAddress();
	}

	/**
	 * 启动Http服务器，启动后会阻塞当前线程
	 */
	public void start() {
		final InetSocketAddress address = getAddress();
		Console.log("Hutool Simple Http Server listen on 【{}:{}】", address.getHostName(), address.getPort());
		this.server.start();
	}
}
