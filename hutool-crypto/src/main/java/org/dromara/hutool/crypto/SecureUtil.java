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

import org.bouncycastle.crypto.AlphabetMapper;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.codec.binary.Hex;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.SystemUtil;
import org.dromara.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import org.dromara.hutool.crypto.asymmetric.RSA;
import org.dromara.hutool.crypto.digest.DigestAlgorithm;
import org.dromara.hutool.crypto.digest.Digester;
import org.dromara.hutool.crypto.digest.MD5;
import org.dromara.hutool.crypto.digest.mac.HMac;
import org.dromara.hutool.crypto.digest.mac.HmacAlgorithm;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;
import org.dromara.hutool.crypto.symmetric.*;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 *
 * @author Looly, Gsealy
 */
public class SecureUtil {

	/** Hutool自定义系统属性：是否解码Hex字符 issue#I90M9D */
	public static String HUTOOL_CRYPTO_DECODE_HEX = "hutool.crypto.decodeHex";

	/**
	 * 生成算法，格式为XXXwithXXX
	 *
	 * @param asymmetricAlgorithm 非对称算法
	 * @param digestAlgorithm     摘要算法
	 * @return 算法
	 * @since 4.4.1
	 */
	public static String generateAlgorithm(final AsymmetricAlgorithm asymmetricAlgorithm, final DigestAlgorithm digestAlgorithm) {
		final String digestPart = (null == digestAlgorithm) ? "NONE" : digestAlgorithm.name();
		return StrUtil.format("{}with{}", digestPart, asymmetricAlgorithm.getValue());
	}

	// ------------------------------------------------------------------- 对称加密算法

	/**
	 * AES加密，生成随机KEY。注意解密时必须使用相同 {@link AES}对象或者使用相同KEY<br>
	 * 例：
	 *
	 * <pre>
	 * AES加密：aes().encrypt(data)
	 * AES解密：aes().decrypt(data)
	 * </pre>
	 *
	 * @return {@link AES}
	 */
	public static AES aes() {
		return new AES();
	}

	/**
	 * AES加密<br>
	 * 例：
	 *
	 * <pre>
	 * AES加密：aes(key).encrypt(data)
	 * AES解密：aes(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link SymmetricCrypto}
	 */
	public static AES aes(final byte[] key) {
		return new AES(key);
	}

	/**
	 * DES加密，生成随机KEY。注意解密时必须使用相同 {@link DES}对象或者使用相同KEY<br>
	 * 例：
	 *
	 * <pre>
	 * DES加密：des().encrypt(data)
	 * DES解密：des().decrypt(data)
	 * </pre>
	 *
	 * @return {@link DES}
	 */
	public static DES des() {
		return new DES();
	}

	/**
	 * DES加密<br>
	 * 例：
	 *
	 * <pre>
	 * DES加密：des(key).encrypt(data)
	 * DES解密：des(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link DES}
	 */
	public static DES des(final byte[] key) {
		return new DES(key);
	}

	/**
	 * DESede加密（又名3DES、TripleDES），生成随机KEY。注意解密时必须使用相同 {@link DESede}对象或者使用相同KEY<br>
	 * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
	 * 例：
	 *
	 * <pre>
	 * DESede加密：desede().encrypt(data)
	 * DESede解密：desede().decrypt(data)
	 * </pre>
	 *
	 * @return {@link DESede}
	 * @since 3.3.0
	 */
	public static DESede desede() {
		return new DESede();
	}

	/**
	 * DESede加密（又名3DES、TripleDES）<br>
	 * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
	 * 例：
	 *
	 * <pre>
	 * DESede加密：desede(key).encrypt(data)
	 * DESede解密：desede(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link DESede}
	 * @since 3.3.0
	 */
	public static DESede desede(final byte[] key) {
		return new DESede(key);
	}

	// ------------------------------------------------------------------- 摘要算法

	/**
	 * MD5加密<br>
	 * 例：
	 *
	 * <pre>
	 * MD5加密：md5().digest(data)
	 * MD5加密并转为16进制字符串：md5().digestHex(data)
	 * </pre>
	 *
	 * @return {@link Digester}
	 */
	public static MD5 md5() {
		return MD5.of();
	}

	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 *
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(final String data) {
		return MD5.of().digestHex(data);
	}

	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 *
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(final InputStream data) {
		return MD5.of().digestHex(data);
	}

	/**
	 * MD5加密文件，生成16进制MD5字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return MD5字符串
	 */
	public static String md5(final File dataFile) {
		return MD5.of().digestHex(dataFile);
	}

	/**
	 * SHA1加密<br>
	 * 例：<br>
	 * SHA1加密：sha1().digest(data)<br>
	 * SHA1加密并转为16进制字符串：sha1().digestHex(data)<br>
	 *
	 * @return {@link Digester}
	 */
	public static Digester sha1() {
		return new Digester(DigestAlgorithm.SHA1);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 */
	public static String sha1(final String data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 */
	public static String sha1(final InputStream data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * SHA1加密文件，生成16进制SHA1字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SHA1字符串
	 */
	public static String sha1(final File dataFile) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(dataFile);
	}

	/**
	 * SHA256加密<br>
	 * 例：<br>
	 * SHA256加密：sha256().digest(data)<br>
	 * SHA256加密并转为16进制字符串：sha256().digestHex(data)<br>
	 *
	 * @return {@link Digester}
	 * @since 4.3.2
	 */
	public static Digester sha256() {
		return new Digester(DigestAlgorithm.SHA256);
	}

	/**
	 * SHA256加密，生成16进制SHA256字符串<br>
	 *
	 * @param data 数据
	 * @return SHA256字符串
	 * @since 4.3.2
	 */
	public static String sha256(final String data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * SHA256加密，生成16进制SHA256字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 * @since 4.3.2
	 */
	public static String sha256(final InputStream data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * SHA256加密文件，生成16进制SHA256字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SHA256字符串
	 * @since 4.3.2
	 */
	public static String sha256(final File dataFile) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(dataFile);
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmac(final HmacAlgorithm algorithm, final String key) {
		return new HMac(algorithm, ByteUtil.toUtf8Bytes(key));
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(final HmacAlgorithm algorithm, final byte[] key) {
		return new HMac(algorithm, key);
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥{@link SecretKey}，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(final HmacAlgorithm algorithm, final SecretKey key) {
		return new HMac(algorithm, key);
	}

	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacMd5(final String key) {
		return hmacMd5(ByteUtil.toUtf8Bytes(key));
	}

	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5(final byte[] key) {
		return new HMac(HmacAlgorithm.HmacMD5, key);
	}

	/**
	 * HmacMD5加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5().digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5() {
		return new HMac(HmacAlgorithm.HmacMD5);
	}

	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacSha1(final String key) {
		return hmacSha1(ByteUtil.toUtf8Bytes(key));
	}

	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1(final byte[] key) {
		return new HMac(HmacAlgorithm.HmacSHA1, key);
	}

	/**
	 * HmacSHA1加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1().digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1() {
		return new HMac(HmacAlgorithm.HmacSHA1);
	}

	/**
	 * HmacSHA256加密器<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256(key).digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256(final String key) {
		return hmacSha256(ByteUtil.toUtf8Bytes(key));
	}

	/**
	 * HmacSHA256加密器<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256(key).digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256(final byte[] key) {
		return new HMac(HmacAlgorithm.HmacSHA256, key);
	}

	/**
	 * HmacSHA256加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256().digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256() {
		return new HMac(HmacAlgorithm.HmacSHA256);
	}

	// ------------------------------------------------------------------- 非称加密算法

	/**
	 * 创建RSA算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa() {
		return new RSA();
	}

	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(final String privateKeyBase64, final String publicKeyBase64) {
		return new RSA(privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(final byte[] privateKey, final byte[] publicKey) {
		return new RSA(privateKey, publicKey);
	}

	/**
	 * 增加加密解密的算法提供者，默认优先使用，例如：
	 *
	 * <pre>
	 * addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	 * </pre>
	 *
	 * @param provider 算法提供者
	 * @since 4.1.22
	 */
	public static void addProvider(final Provider provider) {
		if(ArrayUtil.contains(Security.getProviders(), provider)){
			// 如果已经注册过Provider，不再重新注册
			return;
		}
		Security.insertProviderAt(provider, 0);
	}

	/**
	 * 解码字符串密钥，可支持的编码如下：
	 *
	 * <pre>
	 * 1. Hex（16进制）编码
	 * 1. Base64编码
	 * </pre>
	 *
	 * @param key 被解码的密钥字符串
	 * @return 密钥
	 * @since 4.3.3
	 */
	public static byte[] decode(final String key) {
		if(Objects.isNull(key)){
			return null;
		}

		// issue#I90M9D
		// 某些特殊字符串会无法区分Hex还是Base64，此处使用系统属性强制关闭Hex解析
		final boolean decodeHex = SystemUtil.getBoolean(HUTOOL_CRYPTO_DECODE_HEX, true);
		return (decodeHex && Validator.isHex(key)) ? Hex.decode(key) : Base64.decode(key);
	}

	/**
	 * 创建{@link Cipher}<br>
	 * 当provider为{@code null}时，使用{@link GlobalProviderFactory}查找提供方，找不到使用JDK默认提供方。
	 *
	 * @param algorithm 算法
	 * @return {@link Cipher}
	 * @since 4.5.2
	 */
	public static Cipher createCipher(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final Cipher cipher;
		try {
			cipher = (null == provider) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}

		return cipher;
	}

	/**
	 * 创建{@link MessageDigest}<br>
	 * 当provider为{@code null}时，使用{@link GlobalProviderFactory}查找提供方，找不到使用JDK默认提供方。
	 *
	 * @param algorithm 算法
	 * @param provider  算法提供方，{@code null}表示使用{@link GlobalProviderFactory}找到的提供方。
	 * @return {@link MessageDigest}
	 */
	public static MessageDigest createMessageDigest(final String algorithm, Provider provider) {
		if (null == provider) {
			provider = GlobalProviderFactory.getProvider();
		}

		final MessageDigest messageDigest;
		try {
			messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) :
				MessageDigest.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return messageDigest;
	}

	/**
	 * 创建{@link MessageDigest}，使用JDK默认的Provider<br>
	 *
	 * @param algorithm 算法
	 * @return {@link MessageDigest}
	 */
	public static MessageDigest createJdkMessageDigest(final String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 创建{@link Mac}
	 *
	 * @param algorithm 算法
	 * @return {@link Mac}
	 * @since 4.5.13
	 */
	public static Mac createMac(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final Mac mac;
		try {
			mac = (null == provider) ? Mac.getInstance(algorithm) : Mac.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return mac;
	}

	/**
	 * RC4算法
	 *
	 * @param key 密钥
	 * @return {@link SymmetricCrypto}
	 */
	public static SymmetricCrypto rc4(final byte[] key) {
		return new SymmetricCrypto(SymmetricAlgorithm.RC4, key);
	}

	/**
	 * 强制关闭自定义{@link Provider}的使用，如Bouncy Castle库，全局有效
	 */
	public static void disableCustomProvider() {
		GlobalProviderFactory.setUseCustomProvider(false);
	}

	/**
	 * PBKDF2加密密码
	 *
	 * @param password 密码
	 * @param salt     盐
	 * @return 盐，一般为16位
	 * @since 5.6.0
	 */
	public static String pbkdf2(final char[] password, final byte[] salt) {
		return new PBKDF2().encryptHex(password, salt);
	}

	/**
	 * FPE(Format Preserving Encryption)实现，支持FF1和FF3-1模式。
	 *
	 * @param mode   FPE模式枚举，可选FF1或FF3-1
	 * @param key    密钥，{@code null}表示随机密钥，长度必须是16bit、24bit或32bit
	 * @param mapper Alphabet字典映射，被加密的字符范围和这个映射必须一致，例如手机号、银行卡号等字段可以采用数字字母字典表
	 * @param tweak  Tweak是为了解决因局部加密而导致结果冲突问题，通常情况下将数据的不可变部分作为Tweak
	 * @return {@link FPE}
	 * @since 5.7.12
	 */
	public static FPE fpe(final FPE.FPEMode mode, final byte[] key, final AlphabetMapper mapper, final byte[] tweak) {
		return new FPE(mode, key, mapper, tweak);
	}

	/**
	 * 祖冲之算法集（ZUC-128算法）实现，基于BouncyCastle实现。
	 *
	 * @param key 密钥
	 * @param iv  加盐，长度16bytes，{@code null}是随机加盐
	 * @return {@link ZUC}
	 * @since 5.7.12
	 */
	public static ZUC zuc128(final byte[] key, final byte[] iv) {
		return new ZUC(ZUC.ZUCAlgorithm.ZUC_128, key, iv);
	}

	/**
	 * 祖冲之算法集（ZUC-256算法）实现，基于BouncyCastle实现。
	 *
	 * @param key 密钥
	 * @param iv  加盐，长度25bytes，{@code null}是随机加盐
	 * @return {@link ZUC}
	 * @since 5.7.12
	 */
	public static ZUC zuc256(final byte[] key, final byte[] iv) {
		return new ZUC(ZUC.ZUCAlgorithm.ZUC_256, key, iv);
	}
}
