/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.ssl;

import org.dromara.hutool.core.net.ssl.SSLProtocols;
import org.dromara.hutool.core.net.ssl.SSLUtil;
import org.dromara.hutool.core.net.ssl.SSLContextBuilder;
import org.dromara.hutool.core.net.ssl.TrustAnyHostnameVerifier;
import org.dromara.hutool.core.net.ssl.TrustAnyTrustManager;
import org.dromara.hutool.core.text.StrUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * HTTP请求中SSL相关信息，包括：
 * <ul>
 *     <li>{@link HostnameVerifier}</li>
 *     <li>{@link SSLContext}</li>
 *     <li>{@link X509TrustManager}</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class SSLInfo {

	/**
	 * 默认{@code SSLInfo}，全部为{@code null}，使用客户端引擎默认配置
	 */
	public static final SSLInfo DEFAULT = SSLInfo.of();

	/**
	 * 信任所有的{@code SSLInfo}
	 */
	public static final SSLInfo TRUST_ANY = SSLInfo.of()
			.setHostnameVerifier(TrustAnyHostnameVerifier.INSTANCE)
			.setSslContext(SSLUtil.createTrustAnySSLContext())
			.setTrustManager(TrustAnyTrustManager.INSTANCE);

	/**
	 * 构建{@code SSLInfo}
	 *
	 * @return {@code SSLInfo}
	 */
	public static SSLInfo of() {
		return new SSLInfo();
	}

	/**
	 * 支持的协议类型
	 */
	private String[] protocols;
	/**
	 * HostnameVerifier，用于HTTPS安全连接
	 */
	private HostnameVerifier hostnameVerifier;
	/**
	 * SSLContext，用于HTTPS安全连接
	 */
	private SSLContext sslContext;
	/**
	 * 信任管理器
	 */
	private X509TrustManager trustManager;

	/**
	 * 构造
	 */
	public SSLInfo() {
		if (StrUtil.equalsIgnoreCase("dalvik", System.getProperty("java.vm.name"))) {
			//兼容android低版本SSL连接
			this.protocols = new String[]{
					SSLProtocols.SSLv3,
					SSLProtocols.TLSv1,
					SSLProtocols.TLSv11,
					SSLProtocols.TLSv12};
		}
	}

	/**
	 * 获取所有支持的协议
	 *
	 * @return 协议列表
	 */
	public String[] getProtocols() {
		return protocols;
	}

	/**
	 * 设置协议列表
	 *
	 * @param protocols 协议列表
	 * @return this
	 */
	public SSLInfo setProtocols(final String... protocols) {
		this.protocols = protocols;
		return this;
	}

	/**
	 * 获取{@link HostnameVerifier}
	 *
	 * @return {@link HostnameVerifier}
	 */
	public HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	/**
	 * 设置{@link HostnameVerifier}，信任所有则使用{@link TrustAnyHostnameVerifier}
	 *
	 * @param hostnameVerifier {@link HostnameVerifier}
	 * @return this
	 */
	public SSLInfo setHostnameVerifier(final HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	/**
	 * 获取{@link SSLContext}
	 *
	 * @return {@link SSLContext}
	 */
	public SSLContext getSslContext() {
		return sslContext;
	}

	/**
	 * 设置{@link SSLContext}，可以使用{@link SSLContextBuilder}构建
	 *
	 * @param sslContext {@link SSLContext}
	 * @return this
	 */
	public SSLInfo setSslContext(final SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	/**
	 * 获取{@link X509TrustManager}
	 *
	 * @return {@link X509TrustManager}
	 */
	public X509TrustManager getTrustManager() {
		return trustManager;
	}

	/**
	 * 设置{@link X509TrustManager}，新人所有则使用{@link TrustAnyTrustManager}
	 *
	 * @param trustManager {@link X509TrustManager}
	 * @return this
	 */
	public SSLInfo setTrustManager(final X509TrustManager trustManager) {
		this.trustManager = trustManager;
		return this;
	}

	/**
	 * 获取{@link SSLSocketFactory}
	 *
	 * @return {@link SSLSocketFactory}
	 */
	public SSLSocketFactory getSocketFactory() {
		if(null == this.sslContext){
			return null;
		}
		final SSLSocketFactory factory = this.sslContext.getSocketFactory();
		return new CustomProtocolsSSLFactory(factory, this.protocols);
	}
}
