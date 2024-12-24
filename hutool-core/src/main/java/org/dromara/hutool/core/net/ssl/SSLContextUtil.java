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

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

/**
 * SSL(Secure Sockets Layer 安全套接字协议)中的{@link SSLContext}相关工具封装
 *
 * @author looly
 * @since 5.5.2
 */
public class SSLContextUtil {

	/**
	 * 获取默认的{@link SSLContext}
	 *
	 * @return {@link SSLContext}
	 * @since 6.0.0
	 */
	public static SSLContext getDefault() {
		try {
			return SSLContext.getDefault();
		} catch (final NoSuchAlgorithmException e) {
			throw new HutoolException(e);
		}
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
			.setTrustManagers(TrustManagerUtil.TRUST_ANYS)
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
	 * @param keyStore KeyStore
	 * @param password 密码
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(final KeyStore keyStore, final char[] password) throws IORuntimeException {
		return createSSLContext(
			KeyManagerUtil.getKeyManagers(keyStore, password),
			TrustManagerUtil.getTrustManagers(keyStore)
		);
	}

	/**
	 * 创建和初始化{@link SSLContext}
	 *
	 * @param keyManagers   密钥管理器,{@code null}表示默认
	 * @param trustManagers 信任管理器, {@code null}表示默认
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(final KeyManager[] keyManagers, final TrustManager[] trustManagers) throws IORuntimeException {
		return createSSLContext(null, keyManagers, trustManagers);
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
