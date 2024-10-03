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

package org.dromara.hutool.http.client.engine.httpclient5;

import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
import org.apache.hc.core5.ssl.SSLContexts;
import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.util.ObjUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * TLS Socket 策略构建器
 *
 * @author looly
 * @since 6.0.0
 */
public class TlsSocketStrategyBuilder implements Builder<TlsSocketStrategy> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建TLS Socket策略构建器
	 *
	 * @return TlsSocketStrategyBuilder
	 */
	public static TlsSocketStrategyBuilder of() {
		return new TlsSocketStrategyBuilder();
	}

	private SSLContext sslContext;
	private String[] supportedProtocols;
	private String[] supportedCipherSuites;
	private SSLBufferMode sslBufferMode;
	private HostnameVerificationPolicy hostnameVerificationPolicy;
	private HostnameVerifier hostnameVerifier;

	/**
	 * 设置SSL上下文
	 *
	 * @param sslContext SSL上下文
	 * @return this
	 */
	public TlsSocketStrategyBuilder setSslContext(final SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	/**
	 * 设置支持的协议版本
	 *
	 * @param supportedProtocols 支持的协议版本
	 * @return this
	 */
	public TlsSocketStrategyBuilder setSupportedProtocols(final String[] supportedProtocols) {
		this.supportedProtocols = supportedProtocols;
		return this;
	}

	/**
	 * 设置支持的加密套件
	 *
	 * @param supportedCipherSuites 支持的加密套件
	 * @return this
	 */
	public TlsSocketStrategyBuilder setSupportedCipherSuites(final String[] supportedCipherSuites) {
		this.supportedCipherSuites = supportedCipherSuites;
		return this;
	}

	/**
	 * 设置SSL缓冲模式
	 *
	 * @param sslBufferMode SSL缓冲模式
	 * @return this
	 */
	public TlsSocketStrategyBuilder setSslBufferMode(final SSLBufferMode sslBufferMode) {
		this.sslBufferMode = sslBufferMode;
		return this;
	}

	/**
	 * 设置主机名验证策略
	 *
	 * @param hostnameVerificationPolicy 主机名验证策略
	 * @return this
	 */
	public TlsSocketStrategyBuilder setHostnameVerificationPolicy(final HostnameVerificationPolicy hostnameVerificationPolicy) {
		this.hostnameVerificationPolicy = hostnameVerificationPolicy;
		return this;
	}

	/**
	 * 设置主机名验证器
	 *
	 * @param hostnameVerifier 主机名验证器
	 * @return this
	 */
	public TlsSocketStrategyBuilder setHostnameVerifier(final HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	@Override
	public TlsSocketStrategy build() {
		return new DefaultClientTlsStrategy(
			ObjUtil.defaultIfNull(sslContext, SSLContexts::createDefault),
			supportedProtocols,
			supportedCipherSuites,
			ObjUtil.defaultIfNull(sslBufferMode, SSLBufferMode.STATIC),
			ObjUtil.defaultIfNull(hostnameVerificationPolicy, HostnameVerificationPolicy.BOTH),
			ObjUtil.defaultIfNull(hostnameVerifier, HttpsSupport::getDefaultHostnameVerifier)
		);
	}
}
