/*
 * Copyright (c) 2024 Hutool Team.
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

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.*;

/**
 * {@link KeyManager}相关工具
 *
 * @author Looly
 * @since 6.0.0
 */
public class KeyManagerUtil {

	/**
	 * 获取{@link KeyManagerFactory}，使用全局算法提供者
	 *
	 * @param algorithm 算法，{@code null}表示默认算法，如SunX509
	 * @return {@link KeyManagerFactory}
	 */
	public static KeyManagerFactory getKeyManagerFactory(final String algorithm) {
		return getKeyManagerFactory(algorithm, GlobalProviderFactory.getProvider());
	}

	/**
	 * 获取{@link KeyManagerFactory}
	 *
	 * @param algorithm 算法，{@code null}表示默认算法，如SunX509
	 * @param provider  算法提供者，{@code null}使用JDK默认
	 * @return {@link KeyManagerFactory}
	 */
	public static KeyManagerFactory getKeyManagerFactory(String algorithm, final Provider provider) {
		if (StrUtil.isBlank(algorithm)) {
			algorithm = KeyManagerFactory.getDefaultAlgorithm();
		}

		try {
			return null == provider ? KeyManagerFactory.getInstance(algorithm) : KeyManagerFactory.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 从KeyStore中获取{@link KeyManager}列表
	 *
	 * @param keyStore KeyStore
	 * @param password 密码
	 * @return {@link KeyManager}列表
	 */
	public static KeyManager[] getKeyManagers(final KeyStore keyStore, final char[] password) {
		final KeyManagerFactory keyManagerFactory = getKeyManagerFactory(null);
		try {
			keyManagerFactory.init(keyStore, password);
		} catch (final KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new CryptoException(e);
		}
		return keyManagerFactory.getKeyManagers();
	}
}
