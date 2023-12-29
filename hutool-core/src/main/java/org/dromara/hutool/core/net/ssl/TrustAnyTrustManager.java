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

package org.dromara.hutool.core.net.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * 新任所有信任管理器，默认信任所有客户端和服务端证书<br>
 * 继承{@link X509ExtendedTrustManager}的原因见：<br>
 * https://blog.csdn.net/ghaohao/article/details/79454913
 *
 * <p>注意此类慎用，信任全部可能会有中间人攻击风险。</p>
 *
 * @author Looly
 * @since 5.5.7
 */
public class TrustAnyTrustManager extends X509ExtendedTrustManager {

	/**
	 * 全局单例信任管理器，默认信任所有客户端和服务端证书
	 *
	 * @since 5.7.8
	 */
	public static final TrustAnyTrustManager INSTANCE = new TrustAnyTrustManager();

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
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
