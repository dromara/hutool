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

package org.dromara.hutool.asymmetric;

import org.dromara.hutool.CryptoException;
import org.dromara.hutool.GlobalBouncyCastleProvider;
import org.dromara.hutool.KeyUtil;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * <p>
 * RSA公钥/私钥/签名加密解密
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author Looly
 *
 */
public class RSA extends AsymmetricCrypto {
	private static final long serialVersionUID = 1L;

	/** 默认的RSA算法 */
	private static final AsymmetricAlgorithm ALGORITHM_RSA = AsymmetricAlgorithm.RSA_ECB_PKCS1;

	// ------------------------------------------------------------------ Static method start
	/**
	 * 生成RSA私钥
	 *
	 * @param modulus N特征值
	 * @param privateExponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(final BigInteger modulus, final BigInteger privateExponent) {
		return KeyUtil.generatePrivateKey(ALGORITHM_RSA.getValue(), new RSAPrivateKeySpec(modulus, privateExponent));
	}

	/**
	 * 生成RSA公钥
	 *
	 * @param modulus N特征值
	 * @param publicExponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(final BigInteger modulus, final BigInteger publicExponent) {
		return KeyUtil.generatePublicKey(ALGORITHM_RSA.getValue(), new RSAPublicKeySpec(modulus, publicExponent));
	}
	// ------------------------------------------------------------------ Static method end

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，生成新的私钥公钥对
	 */
	public RSA() {
		super(ALGORITHM_RSA);
	}

	/**
	 * 构造，生成新的私钥公钥对
	 *
	 * @param rsaAlgorithm 自定义RSA算法，例如RSA/ECB/PKCS1Padding
	 */
	public RSA(final String rsaAlgorithm) {
		super(rsaAlgorithm);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public RSA(final String privateKeyStr, final String publicKeyStr) {
		super(ALGORITHM_RSA, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param rsaAlgorithm 自定义RSA算法，例如RSA/ECB/PKCS1Padding
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 * @since 4.5.8
	 */
	public RSA(final String rsaAlgorithm, final String privateKeyStr, final String publicKeyStr) {
		super(rsaAlgorithm, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public RSA(final byte[] privateKey, final byte[] publicKey) {
		super(ALGORITHM_RSA, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param modulus N特征值
	 * @param privateExponent d特征值
	 * @param publicExponent e特征值
	 * @since 3.1.1
	 */
	public RSA(final BigInteger modulus, final BigInteger privateExponent, final BigInteger publicExponent) {
		this(generatePrivateKey(modulus, privateExponent), generatePublicKey(modulus, publicExponent));
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 3.1.1
	 */
	public RSA(final PrivateKey privateKey, final PublicKey publicKey) {
		super(ALGORITHM_RSA, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param rsaAlgorithm 自定义RSA算法，例如RSA/ECB/PKCS1Padding
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 4.5.8
	 */
	public RSA(final String rsaAlgorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		super(rsaAlgorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	@Override
	public byte[] encrypt(final byte[] data, final KeyType keyType) {
		// 在非使用BC库情况下，blockSize使用默认的算法
		if (this.encryptBlockSize < 0 && null == GlobalBouncyCastleProvider.INSTANCE.getProvider()) {
			// 加密数据长度 <= 模长-11
			this.encryptBlockSize = ((RSAKey) getKeyByType(keyType)).getModulus().bitLength() / 8 - 11;
		}
		return super.encrypt(data, keyType);
	}

	@Override
	public byte[] decrypt(final byte[] bytes, final KeyType keyType) {
		// 在非使用BC库情况下，blockSize使用默认的算法
		if (this.decryptBlockSize < 0 && null == GlobalBouncyCastleProvider.INSTANCE.getProvider()) {
			// 加密数据长度 <= 模长-11
			this.decryptBlockSize = ((RSAKey) getKeyByType(keyType)).getModulus().bitLength() / 8;
		}
		return super.decrypt(bytes, keyType);
	}

	@Override
	protected void initCipher() {
		try {
			super.initCipher();
		} catch (final CryptoException e) {
			final Throwable cause = e.getCause();
			if(cause instanceof NoSuchAlgorithmException) {
				// 在Linux下，未引入BC库可能会导致RSA/ECB/PKCS1Padding算法无法找到，此时使用默认算法
				this.algorithm = AsymmetricAlgorithm.RSA.getValue();
				super.initCipher();
			}
			throw e;
		}
	}
}
