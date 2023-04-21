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

package org.dromara.hutool.core.net.ssl;

import org.dromara.hutool.core.exceptions.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.StrUtil;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/**
 * SSL(Secure Sockets Layer 安全套接字协议)相关工具封装
 *
 * @author looly
 * @since 5.5.2
 */
public class SSLUtil {

	/**
	 * 获取指定的{@link X509TrustManager}<br>
	 * 此方法主要用于获取自签证书的{@link X509TrustManager}
	 *
	 * @param keyStore  {@link KeyStore}
	 * @param provider  算法提供者，如bc，{@code null}表示默认
	 * @return {@link X509TrustManager} or {@code null}
	 * @since 6.0.0
	 */
	public static X509TrustManager getTrustManager(final KeyStore keyStore, final Provider provider) {
		return getTrustManager(keyStore, null, provider);
	}

	/**
	 * 获取指定的{@link X509TrustManager}<br>
	 * 此方法主要用于获取自签证书的{@link X509TrustManager}
	 *
	 * @param keyStore  {@link KeyStore}
	 * @param algorithm 算法名称，如"SunX509"，{@code null}表示默认SunX509
	 * @param provider  算法提供者，如bc，{@code null}表示默认SunJSSE
	 * @return {@link X509TrustManager} or {@code null}
	 * @since 6.0.0
	 */
	public static X509TrustManager getTrustManager(final KeyStore keyStore, String algorithm, final Provider provider) {
		final TrustManagerFactory tmf;

		if(StrUtil.isEmpty(algorithm)){
			algorithm = TrustManagerFactory.getDefaultAlgorithm();
		}
		try {
			if(null == provider){
				tmf = TrustManagerFactory.getInstance(algorithm);
			} else{
				tmf = TrustManagerFactory.getInstance(algorithm, provider);
			}
		} catch (final NoSuchAlgorithmException e) {
			throw new HutoolException(e);
		}
		try {
			tmf.init(keyStore);
		} catch (final KeyStoreException e) {
			throw new HutoolException(e);
		}

		final TrustManager[] tms = tmf.getTrustManagers();
		for (final TrustManager tm : tms) {
			if (tm instanceof X509TrustManager) {
				return (X509TrustManager) tm;
			}
		}

		return null;
	}

	/**
	 * 创建{@link SSLContext}，信任全部，协议为TLS
	 *
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createTrustAnySSLContext() throws IORuntimeException {
		return createTrustAnySSLContext(null);
	}

	/**
	 * 创建{@link SSLContext}，信任全部
	 *
	 * @param protocol SSL协议，例如TLS等，{@code null}表示默认TLS
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 * @since 5.7.8
	 */
	public static SSLContext createTrustAnySSLContext(final String protocol) throws IORuntimeException {
		return SSLContextBuilder.of()
			.setProtocol(protocol)
			// 信任所有服务端
			.setTrustManagers(new TrustManager[]{TrustAnyTrustManager.INSTANCE})
			.build();
	}

	/**
	 * 创建{@link SSLContext}
	 *
	 * @param protocol     SSL协议，例如TLS等
	 * @param keyManager   密钥管理器,{@code null}表示默认
	 * @param trustManager 信任管理器, {@code null}表示默认
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(final String protocol, final KeyManager keyManager, final TrustManager trustManager)
		throws IORuntimeException {
		return createSSLContext(protocol,
			keyManager == null ? null : new KeyManager[]{keyManager},
			trustManager == null ? null : new TrustManager[]{trustManager});
	}

	/**
	 * 创建和初始化{@link SSLContext}
	 *
	 * @param protocol      SSL协议，例如TLS等
	 * @param keyManagers   密钥管理器,{@code null}表示默认
	 * @param trustManagers 信任管理器, {@code null}表示默认
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(final String protocol, final KeyManager[] keyManagers, final TrustManager[] trustManagers) throws IORuntimeException {
		return SSLContextBuilder.of()
			.setProtocol(protocol)
			.setKeyManagers(keyManagers)
			.setTrustManagers(trustManagers).build();
	}
}
