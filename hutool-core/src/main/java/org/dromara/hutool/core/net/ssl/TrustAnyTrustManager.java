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

package org.dromara.hutool.core.net.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * 信任所有信任管理器，默认信任所有客户端和服务端证书<br>
 * 继承{@link X509ExtendedTrustManager}的原因见：<br>
 * https://blog.csdn.net/ghaohao/article/details/79454913
 *
 * <p>注意此类慎用，信任全部可能会有中间人攻击风险。</p>
 *
 * @author Looly
 * @since 5.5.7
 */
public class TrustAnyTrustManager extends X509ExtendedTrustManager {

	private static final X509Certificate[] EMPTY_X509_CERTIFICATE_ARRAY = {};

	/**
	 * 全局单例信任管理器，默认信任所有客户端和服务端证书
	 *
	 * @since 5.7.8
	 */
	public static final TrustAnyTrustManager INSTANCE = new TrustAnyTrustManager();

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return EMPTY_X509_CERTIFICATE_ARRAY;
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s, final Socket socket) {
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s, final Socket socket) {
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s, final SSLEngine sslEngine) {
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s, final SSLEngine sslEngine) {
	}
}
