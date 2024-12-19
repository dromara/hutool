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

package org.dromara.hutool.http.client.engine.httpclient4;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.http.ssl.SSLInfo;

/**
 * HttpClient4连接工厂注册器构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ConnectionSocketFactoryRegistryBuilder implements Builder<Registry<ConnectionSocketFactory>> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建
	 *
	 * @return {@code ConnectionSocketFactoryRegistryBuilder}
	 */
	public static ConnectionSocketFactoryRegistryBuilder of() {
		return new ConnectionSocketFactoryRegistryBuilder();
	}

	/**
	 * 构建默认的连接工厂注册器，默认支持SSL
	 *
	 * @param sslInfo SSL配置，{@code null}表示使用默认配置，即{@link SSLConnectionSocketFactory#getSocketFactory()}
	 * @return {@code Registry<ConnectionSocketFactory>}
	 */
	public static Registry<ConnectionSocketFactory> build(final SSLInfo sslInfo) {
		return of().registerPlainHttp().registerHttps(sslInfo).build();
	}

	private final RegistryBuilder<ConnectionSocketFactory> builder;

	/**
	 * 构造
	 */
	public ConnectionSocketFactoryRegistryBuilder() {
		builder = RegistryBuilder.create();
	}

	/**
	 * 注册HTTP协议，使用默认的普通（非加密）连接工厂
	 *
	 * @return this
	 */
	public ConnectionSocketFactoryRegistryBuilder registerPlainHttp() {
		return registerHttp(PlainConnectionSocketFactory.getSocketFactory());
	}

	/**
	 * 注册HTTP协议，使用默认的普通连接工厂
	 *
	 * @param socketFactory {@link ConnectionSocketFactory}
	 * @return this
	 */
	public ConnectionSocketFactoryRegistryBuilder registerHttp(final ConnectionSocketFactory socketFactory) {
		return register("http", socketFactory);
	}

	/**
	 * 注册HTTPS协议
	 *
	 * @param sslInfo SSL配置，{@code null}表示使用默认配置，即{@link SSLConnectionSocketFactory#getSocketFactory()}
	 * @return this
	 */
	public ConnectionSocketFactoryRegistryBuilder registerHttps(final SSLInfo sslInfo) {
		return register("https", buildSocketFactory(sslInfo));
	}

	/**
	 * 注册连接工厂
	 *
	 * @param protocol      协议
	 * @param socketFactory 连接工厂
	 * @return this
	 */
	public ConnectionSocketFactoryRegistryBuilder register(final String protocol, final ConnectionSocketFactory socketFactory) {
		builder.register(protocol, socketFactory);
		return this;
	}

	@Override
	public Registry<ConnectionSocketFactory> build() {
		return builder.build();
	}

	/**
	 * 支持SSL
	 *
	 * @param sslInfo SSL配置，{@code null}表示使用默认配置，即{@link SSLConnectionSocketFactory#getSocketFactory()}
	 * @return SSLConnectionSocketFactory
	 */
	private static SSLConnectionSocketFactory buildSocketFactory(final SSLInfo sslInfo) {
		if (null == sslInfo) {
			return SSLConnectionSocketFactory.getSocketFactory();
		}
		return new SSLConnectionSocketFactory(
			sslInfo.getSslContext(),
			sslInfo.getProtocols(),
			null,
			sslInfo.getHostnameVerifier());
	}
}
