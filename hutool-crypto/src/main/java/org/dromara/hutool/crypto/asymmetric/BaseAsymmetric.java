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

package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
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
	 * 锁
	 */
	protected Lock lock = new ReentrantLock();
	/**
	 * 公钥
	 */
	protected PublicKey publicKey;
	/**
	 * 私钥
	 */
	protected PrivateKey privateKey;
	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm 算法
	 * @param keyPair   密钥对，包括私钥和公钥
	 * @since 6.0.0
	 */
	public BaseAsymmetric(final String algorithm, final KeyPair keyPair) {
		init(algorithm, keyPair);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密（签名）或者解密（校验）
	 *
	 * @param algorithm 算法
	 * @param keyPair   密钥对，包括私钥和公钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	protected T init(final String algorithm, final KeyPair keyPair) {
		this.algorithm = algorithm;

		final PrivateKey privateKey = ObjUtil.apply(keyPair, KeyPair::getPrivate);
		final PublicKey publicKey = ObjUtil.apply(keyPair, KeyPair::getPublic);
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
	 * 生成随机公钥和私钥
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

	/**
	 * 自定义锁，无需锁使用{@link org.dromara.hutool.core.thread.lock.NoLock}
	 *
	 * @param lock 自定义锁
	 * @return this
	 * @since 6.0.0
	 */
	@SuppressWarnings("unchecked")
	public T setLock(final Lock lock) {
		this.lock = lock;
		return (T) this;
	}

	// region ----- getOrSetKeys

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
	// endregion
}
