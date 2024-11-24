/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.GlobalThreadPool;
import org.dromara.hutool.http.server.engine.AbstractServerEngine;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.sun.filter.HttpFilter;
import org.dromara.hutool.http.server.engine.sun.filter.SimpleFilter;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 基于Sun HttpServer的HTTP服务器引擎实现
 *
 * @author looly
 * @since 6.0.0
 */
public class SunHttpServerEngine extends AbstractServerEngine {

	private HttpServer server;
	private List<Filter> filters;

	/**
	 * 构造
	 */
	public SunHttpServerEngine() {
	}

	@Override
	public void start() {
		initEngine();
		this.server.start();
	}

	@Override
	public HttpServer getRawEngine() {
		return this.server;
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #createContext(String, HttpHandler)} </li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SunHttpServerEngine addFilter(final Filter filter) {
		if (null == this.filters) {
			this.filters = new ArrayList<>();
		}
		this.filters.add(filter);
		return this;
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #createContext(String, HttpHandler)} </li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SunHttpServerEngine addFilter(final HttpFilter filter) {
		return addFilter(new SimpleFilter() {
			@Override
			public void doFilter(final HttpExchange httpExchange, final Chain chain) throws IOException {
				final HttpExchangeWrapper httpExchangeWrapper = new HttpExchangeWrapper(httpExchange);
				filter.doFilter(httpExchangeWrapper.getRequest(), httpExchangeWrapper.getResponse(), chain);
			}
		});
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
		if(CollUtil.isNotEmpty(this.filters)){
			context.getFilters().addAll(this.filters);
		}
		return context;
	}

	/**
	 * 设置自定义线程池
	 *
	 * @param executor {@link Executor}
	 * @return this
	 */
	public SunHttpServerEngine setExecutor(final Executor executor) {
		this.server.setExecutor(executor);
		return this;
	}

	@Override
	protected void reset() {
		if (null != this.server) {
			this.server.stop(0);
			this.server = null;
		}
	}

	@Override
	protected void initEngine() {
		final ServerConfig config = this.config;
		final InetSocketAddress address = new InetSocketAddress(config.getHost(), config.getPort());
		final SSLContext sslContext = config.getSslContext();
		try {
			if (null != sslContext) {
				final HttpsServer server = HttpsServer.create(address, 0);
				server.setHttpsConfigurator(new HttpsConfigurator(sslContext));
				this.server = server;
			} else {
				this.server = HttpServer.create(address, 0);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		setExecutor(GlobalThreadPool.getExecutor());
		createContext("/", exchange -> SunHttpServerEngine.this.handler.handle(
			new SunServerRequest(exchange),
			new SunServerResponse(exchange)
		));
	}
}
