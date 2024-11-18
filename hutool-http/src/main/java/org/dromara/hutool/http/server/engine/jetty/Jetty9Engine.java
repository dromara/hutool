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

package org.dromara.hutool.http.server.engine.jetty;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.AbstractServerEngine;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty引擎实现
 *
 * @author Looly
 */
public class Jetty9Engine extends AbstractServerEngine {

	private Server server;

	/**
	 * 构造
	 */
	public Jetty9Engine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Server.class);
	}

	@Override
	public void start() {
		initEngine();
		try {
			this.server.start();
			this.server.join();
		} catch (final Exception e) {
			throw new HttpException(e);
		}
	}

	@Override
	public Server getRawEngine() {
		return this.server;
	}

	@Override
	protected void reset() {
		if (null != this.server) {
			this.server.destroy();
			this.server = null;
		}
	}

	@Override
	protected void initEngine() {
		if (null != this.server) {
			return;
		}

		final ServerConfig config = this.config;
		final Server server = new Server();
		server.addConnector(createConnector(server, config));
		server.setHandler(new AbstractHandler() {
			@Override
			public void handle(final String target, final Request baseRequest,
							   final HttpServletRequest request, final HttpServletResponse response) {
				handler.handle(new Jetty9Request(request), new Jetty9Response(response));
			}
		});
		this.server = server;
	}

	/**
	 * 创建连接器
	 *
	 * @param server 服务器
	 * @param config 配置
	 * @return 连接器
	 */
	private ServerConnector createConnector(final Server server, final ServerConfig config) {
		final ServerConnector connector;

		// 配置
		final HttpConfiguration configuration = new HttpConfiguration();
		final HttpConnectionFactory httpFactory = new HttpConnectionFactory(configuration);

		final SSLContext sslContext = config.getSslContext();
		if (null != sslContext) {
			// 创建HTTPS连接器
			final SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
			sslContextFactory.setSslContext(sslContext);
			final SslConnectionFactory connectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());
			connector = new ServerConnector(server, connectionFactory, httpFactory);
		} else {
			// 创建HTTP连接器
			connector = new ServerConnector(server, httpFactory);
		}

		connector.setHost(config.getHost());
		connector.setPort(config.getPort());

		return connector;
	}
}
