package com.xiaoleilu.hutool.crypto.asymmetric;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import com.xiaoleilu.hutool.codec.BCD;
import com.xiaoleilu.hutool.crypto.CryptoException;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * RSA加密算法
 * 
 * @author Looly
 *
 */
public class RSA extends AsymmetricCrypto {

	private static final AsymmetricAlgorithm ALGORITHM_RSA = AsymmetricAlgorithm.RSA;

	// ------------------------------------------------------------------ Static method start
	/**
	 * 生成RSA私钥
	 * 
	 * @param modulus N特征值
	 * @param privateExponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger privateExponent) {
		return SecureUtil.generatePrivateKey(ALGORITHM_RSA.getValue(), new RSAPrivateKeySpec(modulus, privateExponent));
	}

	/**
	 * 生成RSA公钥
	 * 
	 * @param modulus N特征值
	 * @param publicExponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(BigInteger modulus, BigInteger publicExponent) {
		return SecureUtil.generatePublicKey(ALGORITHM_RSA.getValue(), new RSAPublicKeySpec(modulus, publicExponent));
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
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public RSA(String privateKeyBase64, String publicKeyBase64) {
		super(ALGORITHM_RSA, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public RSA(byte[] privateKey, byte[] publicKey) {
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
	public RSA(BigInteger modulus, BigInteger privateExponent, BigInteger publicExponent) {
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
	public RSA(PrivateKey privateKey, PublicKey publicKey) {
		super(ALGORITHM_RSA, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 */
	public String encryptStr(String data, KeyType keyType) {
		return encryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 * @since 3.1.1
	 */
	public String encryptStr(String data, KeyType keyType, Charset charset) {
		Key key = getKeyByType(keyType);
		// 加密数据长度 <= 模长-11
		int maxBlockSize = ((RSAKey) key).getModulus().bitLength() / 8 - 11;
		final byte[] dataBytes = StrUtil.bytes(data, charset);
		final int inputLen = dataBytes.length;
		
		lock.lock();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			clipher.init(Cipher.ENCRYPT_MODE, key);
			int offSet = 0;
			byte[] cache;
			// 剩余长度
			int remainLength = inputLen;
			// 对数据分段加密
			while (remainLength > 0) {
				cache = clipher.doFinal(dataBytes, offSet, Math.min(remainLength, maxBlockSize));
				out.write(cache, 0, cache.length);
				
				offSet += maxBlockSize;
				remainLength = inputLen - offSet;
			}
			return BCD.bcdToStr(out.toByteArray());
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 分组解密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 */
	public String decryptStr(String data, KeyType keyType) {
		return decryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 3.1.1
	 */
	public String decryptStr(String data, KeyType keyType, Charset charset) {
		final Key key = getKeyByType(keyType);
		// 模长
		final int maxBlockSize = ((RSAKey) key).getModulus().bitLength() / 8;
		byte[] dataBytes = BCD.ascToBcd(StrUtil.bytes(data, charset));
		int inputLen = dataBytes.length;
		
		lock.lock();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			clipher.init(Cipher.DECRYPT_MODE, key);
			int offSet = 0;
			byte[] cache;
			// 剩余长度
			int remainLength = inputLen;
			// 对数据分段解密
			while (remainLength > 0) {
				cache = clipher.doFinal(dataBytes, offSet, Math.min(remainLength, maxBlockSize));
				out.write(cache, 0, cache.length);
				
				offSet += maxBlockSize;
				remainLength = inputLen - offSet;
			}
			return StrUtil.str(out.toByteArray(), charset);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}
}
