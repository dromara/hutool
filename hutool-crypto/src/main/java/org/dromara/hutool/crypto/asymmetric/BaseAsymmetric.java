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

package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;

import java.io.Serializable;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 非对称基础，提供锁、私钥和公钥的持有
 *
 * @param <T> this类型
 * @author Looly
 * @since 3.3.0
 */
public class BaseAsymmetric<T extends BaseAsymmetric<T>> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 算法
	 */
	protected String algorithm;
	/**
	 * 公钥
	 */
	protected PublicKey publicKey;
	/**
	 * 私钥
	 */
	protected PrivateKey privateKey;
	/**
	 * 锁
	 */
	protected final Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public BaseAsymmetric(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		init(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密（签名）或者解密（校验）
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	protected T init(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		this.algorithm = algorithm;

		if (null == privateKey && null == publicKey) {
			initKeys();
		} else {
			if (null != privateKey) {
				this.privateKey = privateKey;
			}
			if (null != publicKey) {
				this.publicKey = publicKey;
			}
		}
		return (T) this;
	}

	/**
	 * 生成公钥和私钥
	 *
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T initKeys() {
		final KeyPair keyPair = KeyUtil.generateKeyPair(this.algorithm);
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		return (T) this;
	}

	// --------------------------------------------------------------------------------- Getters and Setters

	/**
	 * 获得公钥
	 *
	 * @return 获得公钥
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	/**
	 * 获得公钥
	 *
	 * @return 获得公钥
	 */
	public String getPublicKeyBase64() {
		final PublicKey publicKey = getPublicKey();
		return (null == publicKey) ? null : Base64.encode(publicKey.getEncoded());
	}

	/**
	 * 设置公钥
	 *
	 * @param publicKey 公钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setPublicKey(final PublicKey publicKey) {
		this.publicKey = publicKey;
		return (T) this;
	}

	/**
	 * 获得私钥
	 *
	 * @return 获得私钥
	 */
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	/**
	 * 获得私钥
	 *
	 * @return 获得私钥
	 */
	public String getPrivateKeyBase64() {
		final PrivateKey privateKey = getPrivateKey();
		return (null == privateKey) ? null : Base64.encode(privateKey.getEncoded());
	}

	/**
	 * 设置私钥
	 *
	 * @param privateKey 私钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setPrivateKey(final PrivateKey privateKey) {
		this.privateKey = privateKey;
		return (T) this;
	}

	/**
	 * 设置密钥，可以是公钥{@link PublicKey}或者私钥{@link PrivateKey}
	 *
	 * @param key 密钥，可以是公钥{@link PublicKey}或者私钥{@link PrivateKey}
	 * @return this
	 * @since 5.2.0
	 */
	public T setKey(final Key key) {
		Assert.notNull(key, "key must be not null !");

		if (key instanceof PublicKey) {
			return setPublicKey((PublicKey) key);
		} else if (key instanceof PrivateKey) {
			return setPrivateKey((PrivateKey) key);
		}
		throw new CryptoException("Unsupported key type: {}", key.getClass());
	}

	/**
	 * 根据密钥类型获得相应密钥
	 *
	 * @param type 类型 {@link KeyType}
	 * @return {@link Key}
	 */
	protected Key getKeyByType(final KeyType type) {
		switch (type) {
			case PrivateKey:
				if (null == this.privateKey) {
					throw new NullPointerException("Private key must not null when use it !");
				}
				return this.privateKey;
			case PublicKey:
				if (null == this.publicKey) {
					throw new NullPointerException("Public key must not null when use it !");
				}
				return this.publicKey;
		}
		throw new CryptoException("Unsupported key type: " + type);
	}
}
