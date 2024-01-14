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
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.crypto.*;
import org.dromara.hutool.crypto.symmetric.SymmetricAlgorithm;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 非对称加密算法
 *
 * <pre>
 * 1、签名：使用私钥加密，公钥解密。
 * 用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。
 *
 * 2、加密：用公钥加密，私钥解密。
 * 用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * </pre>
 *
 * @author Looly
 */
public class AsymmetricCrypto extends AbstractAsymmetricCrypto<AsymmetricCrypto> {
	private static final long serialVersionUID = 1L;

	/**
	 * Cipher负责完成加密或解密工作
	 */
	protected JceCipher cipher;
	/**
	 * 加密的块大小
	 */
	protected int encryptBlockSize = -1;
	/**
	 * 解密的块大小
	 */
	protected int decryptBlockSize = -1;

	/**
	 * 算法参数
	 */
	private AlgorithmParameterSpec algorithmParameterSpec;
	/**
	 * 自定义随机数
	 */
	private SecureRandom random;
	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	@SuppressWarnings("RedundantCast")
	public AsymmetricCrypto(final AsymmetricAlgorithm algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm 算法
	 */
	@SuppressWarnings("RedundantCast")
	public AsymmetricCrypto(final String algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm     {@link SymmetricAlgorithm}
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr  公钥Hex或Base64表示
	 */
	public AsymmetricCrypto(final AsymmetricAlgorithm algorithm, final String privateKeyStr, final String publicKeyStr) {
		this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public AsymmetricCrypto(final AsymmetricAlgorithm algorithm, final byte[] privateKey, final byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public AsymmetricCrypto(final AsymmetricAlgorithm algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm        非对称加密算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 */
	public AsymmetricCrypto(final String algorithm, final String privateKeyBase64, final String publicKeyBase64) {
		this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public AsymmetricCrypto(final String algorithm, final byte[] privateKey, final byte[] publicKey) {
		this(algorithm, //
			KeyUtil.generatePrivateKey(algorithm, privateKey), //
			KeyUtil.generatePublicKey(algorithm, publicKey)//
		);
	}

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
	public AsymmetricCrypto(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 获取加密块大小
	 *
	 * @return 加密块大小
	 */
	public int getEncryptBlockSize() {
		return encryptBlockSize;
	}

	/**
	 * 设置加密块大小
	 *
	 * @param encryptBlockSize 加密块大小
	 */
	public void setEncryptBlockSize(final int encryptBlockSize) {
		this.encryptBlockSize = encryptBlockSize;
	}

	/**
	 * 获取解密块大小
	 *
	 * @return 解密块大小
	 */
	public int getDecryptBlockSize() {
		return decryptBlockSize;
	}

	/**
	 * 设置解密块大小
	 *
	 * @param decryptBlockSize 解密块大小
	 */
	public void setDecryptBlockSize(final int decryptBlockSize) {
		this.decryptBlockSize = decryptBlockSize;
	}

	/**
	 * 获取{@link AlgorithmParameterSpec}<br>
	 * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
	 *
	 * @return {@link AlgorithmParameterSpec}
	 * @since 5.4.3
	 */
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return this.algorithmParameterSpec;
	}

	/**
	 * 设置{@link AlgorithmParameterSpec}<br>
	 * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
	 *
	 * @param algorithmParameterSpec {@link AlgorithmParameterSpec}
	 * @return this
	 */
	public AsymmetricCrypto setAlgorithmParameterSpec(final AlgorithmParameterSpec algorithmParameterSpec) {
		this.algorithmParameterSpec = algorithmParameterSpec;
		return this;
	}

	/**
	 * 设置随机数生成器，可自定义随机数种子
	 *
	 * @param random 随机数生成器，可自定义随机数种子
	 * @return this
	 * @since 5.7.17
	 */
	public AsymmetricCrypto setRandom(final SecureRandom random) {
		this.random = random;
		return this;
	}

	@Override
	public AsymmetricCrypto init(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		super.init(algorithm, privateKey, publicKey);
		initCipher();
		return this;
	}

	// --------------------------------------------------------------------------------- Encrypt

	@Override
	public byte[] encrypt(final byte[] data, final KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			final JceCipher cipher = initMode(CipherMode.ENCRYPT, key);

			if (this.encryptBlockSize < 0) {
				// 在引入BC库情况下，自动获取块大小
				final int blockSize = cipher.getBlockSize();
				if (blockSize > 0) {
					this.encryptBlockSize = blockSize;
				}
			}

			return doFinal(data, this.encryptBlockSize < 0 ? data.length : this.encryptBlockSize);
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// --------------------------------------------------------------------------------- Decrypt

	@Override
	public byte[] decrypt(final byte[] data, final KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			final JceCipher cipher = initMode(CipherMode.DECRYPT, key);

			if (this.decryptBlockSize < 0) {
				// 在引入BC库情况下，自动获取块大小
				final int blockSize = cipher.getBlockSize();
				if (blockSize > 0) {
					this.decryptBlockSize = blockSize;
				}
			}

			return doFinal(data, this.decryptBlockSize < 0 ? data.length : this.decryptBlockSize);
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// --------------------------------------------------------------------------------- Getters and Setters

	/**
	 * 获得加密或解密器
	 *
	 * @return 加密或解密
	 * @since 5.4.3
	 */
	public Cipher getCipher() {
		return this.cipher.getRaw();
	}

	/**
	 * 初始化{@link Cipher}，默认尝试加载BC库
	 *
	 * @since 4.5.2
	 */
	protected void initCipher() {
		this.cipher = new JceCipher(this.algorithm);
	}

	/**
	 * 加密或解密
	 *
	 * @param data         被加密或解密的内容数据
	 * @param maxBlockSize 最大块（分段）大小
	 * @return 加密或解密后的数据
	 * @throws IllegalBlockSizeException 分段异常
	 * @throws BadPaddingException       padding错误异常
	 * @throws IOException               IO异常，不会被触发
	 */
	private byte[] doFinal(final byte[] data, final int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
		// 模长
		final int dataLength = data.length;

		// 不足分段
		if (dataLength <= maxBlockSize) {
			return getCipher().doFinal(data, 0, dataLength);
		}

		// 分段解密
		return doFinalWithBlock(data, maxBlockSize);
	}

	/**
	 * 分段加密或解密
	 *
	 * @param data         数据
	 * @param maxBlockSize 最大分段的段大小，不能为小于1
	 * @return 加密或解密后的数据
	 * @throws IllegalBlockSizeException 分段异常
	 * @throws BadPaddingException       padding错误异常
	 * @throws IOException               IO异常，不会被触发
	 */
	@SuppressWarnings("resource")
	private byte[] doFinalWithBlock(final byte[] data, final int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
		final int dataLength = data.length;
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();

		int offSet = 0;
		// 剩余长度
		int remainLength = dataLength;
		int blockSize;
		// 对数据分段处理
		while (remainLength > 0) {
			blockSize = Math.min(remainLength, maxBlockSize);
			out.write(getCipher().doFinal(data, offSet, blockSize));

			offSet += blockSize;
			remainLength = dataLength - offSet;
		}

		return out.toByteArray();
	}

	/**
	 * 初始化{@link JceCipher}的模式，如加密模式或解密模式
	 *
	 * @param mode 模式，可选{@link CipherMode#ENCRYPT}或者{@link CipherMode#DECRYPT}
	 * @param key  密钥
	 * @return {@link JceCipher}
	 */
	private JceCipher initMode(final CipherMode mode, final Key key) {
		final JceCipher cipher = this.cipher;
		cipher.init(mode, new JceCipher.JceParameters(key, this.algorithmParameterSpec, this.random));
		return cipher;
	}
}
