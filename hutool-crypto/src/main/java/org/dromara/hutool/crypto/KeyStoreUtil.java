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

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;

/**
 * {@link KeyStore} 相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class KeyStoreUtil {

	/**
	 * Java密钥库(Java Key Store，JKS)KEY_STORE
	 */
	public static final String TYPE_JKS = "JKS";
	/**
	 * jceks
	 */
	public static final String TYPE_JCEKS = "jceks";
	/**
	 * PKCS12是公钥加密标准，它规定了可包含所有私钥、公钥和证书。其以二进制格式存储，也称为 PFX 文件
	 */
	public static final String TYPE_PKCS12 = "pkcs12";

	/**
	 * 读取密钥库(Java Key Store，JKS) KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param keyFile  证书文件
	 * @param password 密码
	 * @return {@link KeyStore}
	 * @since 5.0.0
	 */
	public static KeyStore readJKSKeyStore(final File keyFile, final char[] password) {
		return readKeyStore(TYPE_JKS, keyFile, password);
	}

	/**
	 * 读取密钥库(Java Key Store，JKS) KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readJKSKeyStore(final InputStream in, final char[] password) {
		return readKeyStore(TYPE_JKS, in, password);
	}

	/**
	 * 读取PKCS12 KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存
	 *
	 * @param keyFile  证书文件
	 * @param password 密码
	 * @return {@link KeyStore}
	 * @since 5.0.0
	 */
	public static KeyStore readPKCS12KeyStore(final File keyFile, final char[] password) {
		return readKeyStore(TYPE_PKCS12, keyFile, password);
	}

	/**
	 * 读取PKCS12 KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存
	 *
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 * @since 5.0.0
	 */
	public static KeyStore readPKCS12KeyStore(final InputStream in, final char[] password) {
		return readKeyStore(TYPE_PKCS12, in, password);
	}

	/**
	 * 读取KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param type     类型
	 * @param keyFile  证书文件
	 * @param password 密码，null表示无密码
	 * @return {@link KeyStore}
	 * @since 5.0.0
	 */
	public static KeyStore readKeyStore(final String type, final File keyFile, final char[] password) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(keyFile);
			return readKeyStore(type, in, password);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * 读取KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: <a href="http://snowolf.iteye.com/blog/391931">...</a>
	 *
	 * @param type     类型
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @param password 密码，null表示无密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readKeyStore(final String type, final InputStream in, final char[] password) {
		final KeyStore keyStore = getKeyStore(type);
		try {
			keyStore.load(in, password);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
		return keyStore;
	}

	/**
	 * 获取{@link KeyStore}对象
	 *
	 * @param type 类型
	 * @return {@link KeyStore}
	 */
	public static KeyStore getKeyStore(final String type) {
		final Provider provider = GlobalProviderFactory.getProvider();
		try {
			return null == provider ? KeyStore.getInstance(type) : KeyStore.getInstance(type, provider);
		} catch (final KeyStoreException e) {
			throw new CryptoException(e);
		}
	}
}
