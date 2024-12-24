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

package org.dromara.hutool.crypto.cert;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyStoreUtil;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;

import java.io.File;
import java.io.InputStream;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 数字证书{@link Certificate}相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class CertUtil {

	/**
	 * Certification类型：X.509
	 */
	public static final String TYPE_X509 = "X.509";

	/**
	 * 读取X.509 Certification文件<br>
	 * Certification为证书文件<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param in {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @return {@link KeyStore}
	 */
	public static Certificate readX509Certificate(final InputStream in) {
		return readCertificate(TYPE_X509, in);
	}

	/**
	 * 读取X.509 Certification文件<br>
	 * Certification为证书文件<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param in       {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyStore}
	 */
	public static Certificate readX509Certificate(final InputStream in, final char[] password, final String alias) {
		return readCertificate(TYPE_X509, in, password, alias);
	}

	/**
	 * 读取Certification文件<br>
	 * Certification为证书文件<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param type     类型，例如X.509
	 * @param in       {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyStore}
	 * @since 4.4.1
	 */
	public static Certificate readCertificate(final String type, final InputStream in, final char[] password, final String alias) {
		final KeyStore keyStore = KeyStoreUtil.readKeyStore(type, in, password);
		try {
			return keyStore.getCertificate(alias);
		} catch (final KeyStoreException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 读取Certification文件<br>
	 * Certification为证书文件<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param type 类型，例如X.509
	 * @param in   {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @return {@link Certificate}
	 */
	public static Certificate readCertificate(final String type, final InputStream in) {
		try {
			return getCertificateFactory(type).generateCertificate(in);
		} catch (final CertificateException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 获得 Certification
	 *
	 * @param keyStore {@link KeyStore}
	 * @param alias    别名
	 * @return {@link Certificate}
	 */
	public static Certificate getCertificate(final KeyStore keyStore, final String alias) {
		try {
			return keyStore.getCertificate(alias);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 获取{@link CertificateFactory}
	 *
	 * @param type 类型，例如X.509
	 * @return {@link KeyPairGenerator}
	 * @since 4.5.0
	 */
	public static CertificateFactory getCertificateFactory(final String type) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final CertificateFactory factory;
		try {
			factory = (null == provider) ? CertificateFactory.getInstance(type) : CertificateFactory.getInstance(type, provider);
		} catch (final CertificateException e) {
			throw new CryptoException(e);
		}
		return factory;
	}

	/**
	 * 判断一个证书是否是自签名的，即证书由自己签发。
	 * @param cert 证书
	 * @return true表示自签名的，false表示非自签名的
	 */
	public static boolean isSelfSigned(final X509Certificate cert) {
		return isSignedBy(cert, cert);
	}

	/**
	 * 验证一个证书是否由另一个证书签发。<br>
	 * 来自：sun.security.tools.KeyStoreUtil
	 *
	 * @param end 需要验证的终端证书
	 * @param ca  用于验证的CA证书
	 * @return 如果终端证书由CA证书签发，则返回true，否则返回false
	 */
	public static boolean isSignedBy(final X509Certificate end, final X509Certificate ca) {
		// 检查CA证书的主题和终端证书的颁发者是否相同
		if (!ca.getSubjectX500Principal().equals(end.getIssuerX500Principal())) {
			return false;
		}
		try {
			// 使用CA证书的公钥验证终端证书
			end.verify(ca.getPublicKey());
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
}
