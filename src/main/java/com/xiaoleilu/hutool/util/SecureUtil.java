package com.xiaoleilu.hutool.util;

import java.io.File;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.lang.Base64;

/**
 * 安全相关工具类
 * 
 * @author xiaoleilu
 *
 */
public class SecureUtil {

	public static final String HMAC_SHA1 = "HmacSHA1";

	public static final String RIPEMD128 = "RIPEMD128";
	public static final String RIPEMD160 = "RIPEMD160";
	
	
	//------------------------------------------------------------------- MD5
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(byte[])
	 */
	public static String md5(byte[] source) {
		return DigestUtil.md5Hex(source);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param file 被加密的文件
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(File)
	 */
	public static String md5(File file) {
		return DigestUtil.md5Hex(file);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(String, String)
	 */
	public static String md5(String source, String charset) {
		return DigestUtil.md5Hex(source, charset);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(String, String)
	 */
	public static String md5(String source) {
		return DigestUtil.md5Hex(source);
	}
	
	//------------------------------------------------------------------- SHA-1

	/**
	 * SHA-1算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @see DigestUtil#sha1Hex(String, String)
	 */
	public static String sha1(String source, String charset) {
		return DigestUtil.sha1Hex(source, charset);
	}
	
	/**
	 * SHA-1算法加密
	 * 
	 * @param file 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#sha1Hex(File)
	 */
	public static String sha1(File file) {
		return DigestUtil.sha1Hex(file);
	}

	// ------------------------------------------------------------------------ MAC
	/**
	 * MAC 算法加密
	 * 
	 * @param algorithm 算法
	 * @param key 加密使用的key
	 * @param data 待加密的数据
	 * @return 被加密后的bytes
	 */
	public static byte[] mac(String algorithm, byte[] key, byte[] data) {
		Mac mac = null;
		try {
			mac = Mac.getInstance(algorithm);
			mac.init(new SecretKeySpec(key, algorithm));
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(e, "No such algorithm: {}", algorithm);
		} catch (InvalidKeyException e) {
			throw new UtilException(e, "Invalid key: {}", key);
		}
		return mac == null ? null : mac.doFinal(data);
	}

	/**
	 * MAC SHA-1算法加密
	 * 
	 * @param key 加密使用的key
	 * @param data 待加密的数据
	 * @return 被加密后的bytes
	 */
	public static byte[] hmacSha1(byte[] key, byte[] data) {
		return mac(HMAC_SHA1, key, data);
	}

	/**
	 * MAC SHA-1算法加密
	 * 
	 * @param key 加密使用的key
	 * @param data 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String hmacSha1(String key, String data, String charset) {
		final Charset charsetObj = Charset.forName(charset);
		final byte[] bytes = hmacSha1(key.getBytes(charsetObj), data.getBytes(charsetObj));
		return Base64.encode(bytes, charset);
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @param algorithm 算法
	 * @param charset 字符集
	 * @return key
	 * @throws Exception
	 */
	public static String initMacKey(String algorithm, String charset) throws Exception {
		return Base64.encode(KeyGenerator.getInstance(algorithm).generateKey().getEncoded(), charset);
	}

	/**
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
