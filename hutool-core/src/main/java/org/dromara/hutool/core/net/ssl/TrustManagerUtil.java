/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.text.StrUtil;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/**
 * {@link TrustManager}相关工具类
 *
 * @author Looly
 * @since 6.0.0
 */
public class TrustManagerUtil {

	/**
	 * 信任所有
	 */
	public static final X509TrustManager[] TRUST_ANYS = {TrustAnyTrustManager.INSTANCE};

	/**
	 * 获取默认的{@link TrustManager}，为SunX509<br>
	 * 此方法主要用于获取自签证书的{@link X509TrustManager}
	 *
	 * @return {@link X509TrustManager} or {@code null}
	 * @since 6.0.0
	 */
	public static X509TrustManager getDefaultTrustManager() {
		return getTrustManager(null, null);
	}

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
	public static X509TrustManager getTrustManager(final KeyStore keyStore, final String algorithm, final Provider provider) {
		final TrustManager[] tms = getTrustManagers(keyStore, algorithm, provider);
		for (final TrustManager tm : tms) {
			if (tm instanceof X509TrustManager) {
				return (X509TrustManager) tm;
			}
		}

		return null;
	}

	/**
	 * 获取默认的{@link TrustManager}，为SunX509<br>
	 * 此方法主要用于获取自签证书的{@link TrustManager}
	 *
	 * @return {@link X509TrustManager} or {@code null}
	 * @since 6.0.0
	 */
	public static TrustManager[] getDefaultTrustManagers() {
		return getTrustManagers(null, null, null);
	}

	/**
	 * 获取指定的{@link TrustManager}<br>
	 * 此方法主要用于获取自签证书的{@link TrustManager}
	 *
	 * @param keyStore  {@link KeyStore}
	 * @param algorithm 算法名称，如"SunX509"，{@code null}表示默认SunX509
	 * @param provider  算法提供者，如bc，{@code null}表示默认SunJSSE
	 * @return {@link TrustManager} or {@code null}
	 * @since 6.0.0
	 */
	public static TrustManager[] getTrustManagers(final KeyStore keyStore, String algorithm, final Provider provider) {
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

		return tmf.getTrustManagers();
	}
}
