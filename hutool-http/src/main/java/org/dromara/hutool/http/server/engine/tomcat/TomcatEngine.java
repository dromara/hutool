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

package org.dromara.hutool.http.server.engine.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ValveBase;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.AbstractServerEngine;

import javax.net.ssl.SSLContext;

/**
 * Tomcat引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class TomcatEngine extends AbstractServerEngine {

	private Tomcat tomcat;

	/**
	 * 构造
	 */
	public TomcatEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Tomcat.class);
		// 初始化过程中遇到任何严重错误时会立即退出
		System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
	}

	@Override
	public void start() {
		initEngine();
		try {
			this.tomcat.start();
		} catch (final LifecycleException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public Tomcat getRawEngine() {
		return this.tomcat;
	}

	@Override
	protected void reset() {
		if (null != this.tomcat) {
			try {
				this.tomcat.destroy();
			} catch (final LifecycleException e) {
				throw new HttpException(e);
			}
			this.tomcat = null;
		}
	}

	@Override
	protected void initEngine() {
		if (null != this.tomcat) {
			return;
		}

		final Tomcat tomcat = new Tomcat();
		tomcat.setHostname(config.getHost());
		tomcat.setBaseDir(config.getRoot());

		tomcat.setConnector(createConnector());
		initContext(tomcat);

		this.tomcat = tomcat;
	}

	/**
	 * 创建Connector
	 *
	 * @return Connector
	 */
	private Connector createConnector() {
		final ServerConfig config = this.config;
		final Http11NioProtocol protocol = new Http11NioProtocol();
		final int maxHeaderSize = config.getMaxHeaderSize();
		if(maxHeaderSize > 0){
			protocol.setMaxHttpHeaderSize(maxHeaderSize);
		}
		final int maxThreads = config.getMaxThreads();
		if(maxThreads > 0){
			protocol.setMaxThreads(maxThreads);
		}

		final Connector connector = new Connector(protocol);
		connector.setPort(config.getPort());
		final int maxBodySize = (int) config.getMaxBodySize();
		if(maxBodySize > 0){
			connector.setMaxPostSize(maxBodySize);
		}

		// SSL配置
		final SSLContext sslContext = config.getSslContext();
		if(null != sslContext){
			final SSLHostConfig sslHostConfig = new SSLHostConfig();
			final SSLHostConfigCertificate sslHostConfigCertificate =
				new SSLHostConfigCertificate(sslHostConfig, SSLHostConfigCertificate.Type.RSA);
			sslHostConfigCertificate.setSslContext(new JSSESSLContext(sslContext));
			sslHostConfig.addCertificate(sslHostConfigCertificate);
			connector.addSslHostConfig(sslHostConfig);
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(config.getPort());
		}

		return connector;
	}

	/**
	 * 初始化Context
	 *
	 * @param tomcat Tomcat
	 */
	private void initContext(final Tomcat tomcat) {
		final Context context = tomcat.addContext(StrUtil.EMPTY, null);
		context.getPipeline().addValve(new ValveBase() {
			@Override
			public void invoke(final Request request, final Response response) {
				handler.handle(new TomcatRequest(request), new TomcatResponse(response));
				//getNext().invoke(request, response);
			}
		});
	}
}
