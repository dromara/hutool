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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import org.dromara.hutool.crypto.bc.ECKeyUtil;
import org.dromara.hutool.crypto.bc.SmUtil;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;
import org.dromara.hutool.crypto.symmetric.SymmetricAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.*;

/**
 * 密钥工具类
 *
 * <p>
 * 包括:
 * <pre>
 * 1、生成密钥（单密钥、密钥对）
 * 2、读取密钥文件
 * </pre>
 *
 * @author looly, Gsealy
 * @since 4.4.1
 */
public class KeyUtil {

	/**
	 * 默认密钥字节数
	 *
	 * <pre>
	 * RSA/DSA
	 * Default Keysize 1024
	 * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
	 * </pre>
	 */
	public static final int DEFAULT_KEY_SIZE = 1024;

	// region ----- generateKey

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 *
	 * @param algorithm 算法，支持PBE算法
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(final String algorithm) {
		return generateKey(algorithm, -1);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成<br>
	 * 当指定keySize&lt;0时，AES默认长度为128，其它算法不指定。
	 *
	 * @param algorithm 算法，支持PBE算法
	 * @param keySize   密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
	 * @return {@link SecretKey}
	 * @since 3.1.2
	 */
	public static SecretKey generateKey(final String algorithm, final int keySize) {
		return generateKey(algorithm, keySize, null);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成<br>
	 * 当指定keySize&lt;0时，AES默认长度为128，其它算法不指定。
	 *
	 * @param algorithm 算法，支持PBE算法
	 * @param keySize   密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
	 * @param random    随机数生成器，null表示默认
	 * @return {@link SecretKey}
	 * @since 5.5.2
	 */
	public static SecretKey generateKey(String algorithm, int keySize, final SecureRandom random) {
		algorithm = getMainAlgorithm(algorithm);

		final KeyGenerator keyGenerator = getKeyGenerator(algorithm);
		if (keySize <= 0 && SymmetricAlgorithm.AES.getValue().equals(algorithm)) {
			// 对于AES的密钥，除非指定，否则强制使用128位
			keySize = 128;
		}

		if (keySize > 0) {
			if (null == random) {
				keyGenerator.init(keySize);
			} else {
				keyGenerator.init(keySize, random);
			}
		}
		return keyGenerator.generateKey();
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 *
	 * @param algorithm 算法
	 * @param key       密钥，如果为{@code null} 自动生成随机密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(final String algorithm, final byte[] key) {
		Assert.notBlank(algorithm, "Algorithm is blank!");
		final SecretKey secretKey;
		if (algorithm.startsWith("PBE")) {
			// PBE密钥
			secretKey = generatePBEKey(algorithm, (null == key) ? null : StrUtil.utf8Str(key).toCharArray());
		} else if (algorithm.startsWith("DES")) {
			// DES密钥
			secretKey = generateDESKey(algorithm, key);
		} else {
			// 其它算法密钥
			secretKey = (null == key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
		}
		return secretKey;
	}

	/**
	 * 生成 {@link SecretKey}
	 *
	 * @param algorithm DES算法，包括DES、DESede等
	 * @param key       密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateDESKey(final String algorithm, final byte[] key) {
		if (StrUtil.isBlank(algorithm) || !algorithm.startsWith("DES")) {
			throw new CryptoException("Algorithm [{}] is not a DES algorithm!", algorithm);
		}

		final SecretKey secretKey;
		if (null == key) {
			secretKey = generateKey(algorithm);
		} else {
			secretKey = generateKey(algorithm,
				SpecUtil.createKeySpec(algorithm, key));
		}
		return secretKey;
	}

	/**
	 * 生成PBE {@link SecretKey}
	 *
	 * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
	 * @param password  口令
	 * @return {@link SecretKey}
	 */
	public static SecretKey generatePBEKey(final String algorithm, char[] password) {
		if (StrUtil.isBlank(algorithm) || !algorithm.startsWith("PBE")) {
			throw new CryptoException("Algorithm [{}] is not a PBE algorithm!", algorithm);
		}

		if (null == password) {
			password = RandomUtil.randomStringLower(32).toCharArray();
		}
		return generateKey(algorithm, SpecUtil.createPBEKeySpec(password));
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
	 *
	 * @param algorithm 算法
	 * @param keySpec   {@link KeySpec}
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(final String algorithm, final KeySpec keySpec) {
		final SecretKeyFactory keyFactory = getSecretKeyFactory(algorithm);
		try {
			return keyFactory.generateSecret(keySpec);
		} catch (final InvalidKeySpecException e) {
			throw new CryptoException(e);
		}
	}
	// endregion

	/**
	 * 检查{@link KeyPair} 是否为空，空的条件是：
	 * <ul>
	 *     <li>keyPair本身为{@code null}</li>
	 *     <li>{@link KeyPair#getPrivate()}和{@link KeyPair#getPublic()}都为{@code null}</li>
	 * </ul>
	 *
	 * @param keyPair 密钥对
	 * @return 是否为空
	 */
	// region ----- keyPair
	public static boolean isEmpty(final KeyPair keyPair) {
		if (null == keyPair) {
			return false;
		}
		return null != keyPair.getPrivate() || null != keyPair.getPublic();
	}

	/**
	 * 生成RSA私钥，仅用于非对称加密<br>
	 * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param key 密钥，必须为DER编码存储
	 * @return RSA私钥 {@link PrivateKey}
	 * @since 4.5.2
	 */
	public static PrivateKey generateRSAPrivateKey(final byte[] key) {
		return generatePrivateKey(AsymmetricAlgorithm.RSA.getValue(), key);
	}

	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param algorithm 算法，如RSA、EC、SM2等
	 * @param key       密钥，PKCS#8格式
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(final String algorithm, final byte[] key) {
		if (null == key) {
			return null;
		}
		return generatePrivateKey(algorithm, new PKCS8EncodedKeySpec(key));
	}

	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param algorithm 算法，如RSA、EC、SM2等
	 * @param keySpec   {@link KeySpec}
	 * @return 私钥 {@link PrivateKey}
	 * @since 3.1.1
	 */
	public static PrivateKey generatePrivateKey(String algorithm, final KeySpec keySpec) {
		if (null == keySpec) {
			return null;
		}
		algorithm = getAlgorithmAfterWith(algorithm);
		try {
			return getKeyFactory(algorithm).generatePrivate(keySpec);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 生成私钥，仅用于非对称加密
	 *
	 * @param keyStore {@link KeyStore}
	 * @param alias    别名
	 * @param password 密码
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(final KeyStore keyStore, final String alias, final char[] password) {
		try {
			return (PrivateKey) keyStore.getKey(alias, password);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 生成RSA公钥，仅用于非对称加密<br>
	 * 采用X509证书规范<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param key 密钥，必须为DER编码存储
	 * @return 公钥 {@link PublicKey}
	 * @since 4.5.2
	 */
	public static PublicKey generateRSAPublicKey(final byte[] key) {
		return generatePublicKey(AsymmetricAlgorithm.RSA.getValue(), key);
	}

	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 采用X509证书规范<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param algorithm 算法
	 * @param key       密钥，必须为DER编码存储
	 * @return 公钥 {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(final String algorithm, final byte[] key) {
		if (null == key) {
			return null;
		}
		return generatePublicKey(algorithm, new X509EncodedKeySpec(key));
	}

	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory">...</a>
	 *
	 * @param algorithm 算法
	 * @param keySpec   {@link KeySpec}
	 * @return 公钥 {@link PublicKey}
	 * @since 3.1.1
	 */
	public static PublicKey generatePublicKey(String algorithm, final KeySpec keySpec) {
		if (null == keySpec) {
			return null;
		}
		algorithm = getAlgorithmAfterWith(algorithm);
		try {
			return getKeyFactory(algorithm).generatePublic(keySpec);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 通过RSA私钥生成RSA公钥
	 *
	 * @param privateKey RSA私钥
	 * @return RSA公钥，null表示私钥不被支持
	 * @since 5.3.6
	 */
	public static PublicKey getRSAPublicKey(final PrivateKey privateKey) {
		if (privateKey instanceof RSAPrivateCrtKey) {
			final RSAPrivateCrtKey privk = (RSAPrivateCrtKey) privateKey;
			return getRSAPublicKey(privk.getModulus(), privk.getPublicExponent());
		}
		return null;
	}

	/**
	 * 获得RSA公钥对象
	 *
	 * @param modulus        Modulus
	 * @param publicExponent Public Exponent
	 * @return 公钥
	 * @since 5.3.6
	 */
	public static PublicKey getRSAPublicKey(final String modulus, final String publicExponent) {
		return getRSAPublicKey(
			new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
	}

	/**
	 * 获得RSA公钥对象
	 *
	 * @param modulus        Modulus
	 * @param publicExponent Public Exponent
	 * @return 公钥
	 * @since 5.3.6
	 */
	public static PublicKey getRSAPublicKey(final BigInteger modulus, final BigInteger publicExponent) {
		final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
		try {
			return getKeyFactory(AsymmetricAlgorithm.RSA.getValue()).generatePublic(publicKeySpec);
		} catch (final InvalidKeySpecException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(final String algorithm) {
		int keySize = DEFAULT_KEY_SIZE;
		if ("ECIES".equalsIgnoreCase(algorithm)) {
			// ECIES算法对KEY的长度有要求，此处默认256
			keySize = 256;
		}

		return generateKeyPair(algorithm, keySize);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(final String algorithm, final int keySize) {
		return generateKeyPair(algorithm, keySize, null);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度
	 * @param seed      种子
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(final String algorithm, final int keySize, final byte[] seed) {
		// SM2算法需要单独定义其曲线生成
		if ("SM2".equalsIgnoreCase(algorithm)) {
			final ECGenParameterSpec sm2p256v1 = new ECGenParameterSpec(SmUtil.SM2_CURVE_NAME);
			return generateKeyPair(algorithm, keySize, seed, sm2p256v1);
		}

		return generateKeyPair(algorithm, keySize, seed, (AlgorithmParameterSpec[]) null);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * @param algorithm 非对称加密算法
	 * @param params    {@link AlgorithmParameterSpec}
	 * @return {@link KeyPair}
	 * @since 4.3.3
	 */
	public static KeyPair generateKeyPair(final String algorithm, final AlgorithmParameterSpec params) {
		return generateKeyPair(algorithm, null, params);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * @param algorithm 非对称加密算法
	 * @param param     {@link AlgorithmParameterSpec}
	 * @param seed      种子
	 * @return {@link KeyPair}
	 * @since 4.3.3
	 */
	public static KeyPair generateKeyPair(final String algorithm, final byte[] seed, final AlgorithmParameterSpec param) {
		return generateKeyPair(algorithm, DEFAULT_KEY_SIZE, seed, param);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * <p>
	 * 对于非对称加密算法，密钥长度有严格限制，具体如下：
	 *
	 * <p>
	 * <b>RSA：</b>
	 * <pre>
	 * RS256、PS256：2048 bits
	 * RS384、PS384：3072 bits
	 * RS512、RS512：4096 bits
	 * </pre>
	 *
	 * <p>
	 * <b>EC（Elliptic Curve）：</b>
	 * <pre>
	 * EC256：256 bits
	 * EC384：384 bits
	 * EC512：512 bits
	 * </pre>
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度（单位bit）
	 * @param seed      种子
	 * @param params    {@link AlgorithmParameterSpec}
	 * @return {@link KeyPair}
	 * @since 4.3.3
	 */
	public static KeyPair generateKeyPair(final String algorithm, final int keySize, final byte[] seed, final AlgorithmParameterSpec... params) {
		return generateKeyPair(algorithm, keySize, RandomUtil.createSecureRandom(seed), params);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：<a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">...</a>
	 *
	 * <p>
	 * 对于非对称加密算法，密钥长度有严格限制，具体如下：
	 *
	 * <p>
	 * <b>RSA：</b>
	 * <pre>
	 * RS256、PS256：2048 bits
	 * RS384、PS384：3072 bits
	 * RS512、RS512：4096 bits
	 * </pre>
	 *
	 * <p>
	 * <b>EC（Elliptic Curve）：</b>
	 * <pre>
	 * EC256：256 bits
	 * EC384：384 bits
	 * EC512：512 bits
	 * </pre>
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度（单位bit）
	 * @param random    {@link SecureRandom} 对象，创建时可选传入seed
	 * @param params    {@link AlgorithmParameterSpec}
	 * @return {@link KeyPair}
	 * @since 4.6.5
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize, final SecureRandom random, final AlgorithmParameterSpec... params) {
		algorithm = getAlgorithmAfterWith(algorithm);
		final KeyPairGenerator keyPairGen = getKeyPairGenerator(algorithm);

		// 密钥模（modulus ）长度初始化定义
		if (keySize > 0) {
			// key长度适配修正
			if ("EC".equalsIgnoreCase(algorithm) && keySize > 256) {
				// 对于EC（EllipticCurve）算法，密钥长度有限制，在此使用默认256
				keySize = 256;
			}
			if (null != random) {
				keyPairGen.initialize(keySize, random);
			} else {
				keyPairGen.initialize(keySize);
			}
		}

		// 自定义初始化参数
		if (ArrayUtil.isNotEmpty(params)) {
			for (final AlgorithmParameterSpec param : params) {
				if (null == param) {
					continue;
				}
				try {
					if (null != random) {
						keyPairGen.initialize(param, random);
					} else {
						keyPairGen.initialize(param);
					}
				} catch (final InvalidAlgorithmParameterException e) {
					throw new CryptoException(e);
				}
			}
		}
		return keyPairGen.generateKeyPair();
	}

	/**
	 * 从KeyStore中获取私钥公钥
	 *
	 * @param type     类型
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyPair}
	 * @since 4.4.1
	 */
	public static KeyPair getKeyPair(final String type, final InputStream in, final char[] password, final String alias) {
		final KeyStore keyStore = KeyStoreUtil.readKeyStore(type, in, password);
		return getKeyPair(keyStore, password, alias);
	}

	/**
	 * 从KeyStore中获取私钥公钥
	 *
	 * @param keyStore {@link KeyStore}
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyPair}
	 * @since 4.4.1
	 */
	public static KeyPair getKeyPair(final KeyStore keyStore, final char[] password, final String alias) {
		final PublicKey publicKey;
		final PrivateKey privateKey;
		try {
			publicKey = keyStore.getCertificate(alias).getPublicKey();
			privateKey = (PrivateKey) keyStore.getKey(alias, password);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
		return new KeyPair(publicKey, privateKey);
	}

	/**
	 * 获取{@link KeyPairGenerator}
	 *
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyPairGenerator}
	 * @since 4.4.3
	 */
	public static KeyPairGenerator getKeyPairGenerator(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final KeyPairGenerator keyPairGen;
		try {
			keyPairGen = (null == provider) //
				? KeyPairGenerator.getInstance(getMainAlgorithm(algorithm)) //
				: KeyPairGenerator.getInstance(getMainAlgorithm(algorithm), provider);//
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return keyPairGen;
	}
	// endregion

	/**
	 * 获取{@link KeyFactory}
	 *
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyFactory}
	 * @since 4.4.4
	 */
	public static KeyFactory getKeyFactory(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final KeyFactory keyFactory;
		try {
			keyFactory = (null == provider) //
				? KeyFactory.getInstance(getMainAlgorithm(algorithm)) //
				: KeyFactory.getInstance(getMainAlgorithm(algorithm), provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return keyFactory;
	}

	/**
	 * 获取{@link SecretKeyFactory}
	 *
	 * @param algorithm 对称加密算法
	 * @return {@link KeyFactory}
	 * @since 4.5.2
	 */
	public static SecretKeyFactory getSecretKeyFactory(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final SecretKeyFactory keyFactory;
		try {
			keyFactory = (null == provider) //
				? SecretKeyFactory.getInstance(getMainAlgorithm(algorithm)) //
				: SecretKeyFactory.getInstance(getMainAlgorithm(algorithm), provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return keyFactory;
	}

	/**
	 * 获取{@link KeyGenerator}
	 *
	 * @param algorithm 对称加密算法
	 * @return {@link KeyGenerator}
	 * @since 4.5.2
	 */
	public static KeyGenerator getKeyGenerator(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();
		final KeyGenerator generator;
		try {
			generator = (null == provider) //
				? KeyGenerator.getInstance(getMainAlgorithm(algorithm)) //
				: KeyGenerator.getInstance(getMainAlgorithm(algorithm), provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return generator;
	}

	/**
	 * 获取主体算法名，例如RSA/ECB/PKCS1Padding的主体算法是RSA
	 *
	 * @param algorithm XXXwithXXX算法
	 * @return 主体算法名
	 * @since 4.5.2
	 */
	public static String getMainAlgorithm(final String algorithm) {
		Assert.notBlank(algorithm, "Algorithm must be not blank!");
		final int slashIndex = algorithm.indexOf(CharUtil.SLASH);
		if (slashIndex > 0) {
			return algorithm.substring(0, slashIndex);
		}
		return algorithm;
	}

	/**
	 * 获取用于密钥生成的算法<br>
	 * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
	 *
	 * @param algorithm XXXwithXXX算法
	 * @return 算法
	 */
	public static String getAlgorithmAfterWith(String algorithm) {
		Assert.notNull(algorithm, "algorithm must be not null !");

		if (StrUtil.startWithIgnoreCase(algorithm, "ECIESWith")) {
			return "EC";
		}

		final int indexOfWith = StrUtil.lastIndexOfIgnoreCase(algorithm, "with");
		if (indexOfWith > 0) {
			algorithm = StrUtil.subSuf(algorithm, indexOfWith + "with".length());
		}
		if ("ECDSA".equalsIgnoreCase(algorithm)
			|| "SM2".equalsIgnoreCase(algorithm)
			|| "ECIES".equalsIgnoreCase(algorithm)
		) {
			algorithm = "EC";
		}
		return algorithm;
	}

	/**
	 * 读取X.509 Certification文件中的公钥<br>
	 * Certification为证书文件<br>
	 * see: <a href="https://www.cnblogs.com/yinliang/p/10115519.html">...</a>
	 *
	 * @param in {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(File)} 读取
	 * @return {@link KeyStore}
	 * @since 4.5.2
	 */
	public static PublicKey readPublicKeyFromCert(final InputStream in) {
		final Certificate certificate = CertUtil.readX509Certificate(in);
		if (null != certificate) {
			return certificate.getPublicKey();
		}
		return null;
	}

	/**
	 * 编码压缩EC公钥（基于BouncyCastle）<br>
	 * 见：<a href="https://www.cnblogs.com/xinzhao/p/8963724.html">...</a>
	 *
	 * @param publicKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
	 * @return 压缩得到的X
	 * @since 4.4.4
	 */
	public static byte[] encodeECPublicKey(final PublicKey publicKey) {
		return ECKeyUtil.encodeECPublicKey(publicKey);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：<a href="https://www.cnblogs.com/xinzhao/p/8963724.html">...</a>
	 *
	 * @param encode    压缩公钥
	 * @param curveName EC曲线名
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(final String encode, final String curveName) {
		return ECKeyUtil.decodeECPoint(encode, curveName);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：<a href="https://www.cnblogs.com/xinzhao/p/8963724.html">...</a>
	 *
	 * @param encodeByte 压缩公钥
	 * @param curveName  EC曲线名
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(final byte[] encodeByte, final String curveName) {
		return ECKeyUtil.decodeECPoint(encodeByte, curveName);
	}

	/**
	 * 将密钥编码为Base64格式
	 *
	 * @param key 密钥
	 * @return Base64格式密钥
	 * @since 5.7.22
	 */
	public static String toBase64(final Key key) {
		return Base64.encode(key.getEncoded());
	}
}
