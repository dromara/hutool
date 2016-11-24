package com.xiaoleilu.hutool.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Base64;

/**
 * 安全相关工具类
 * 
 * @author xiaoleilu
 *
 */
public class SecureUtil {

	public static final String MD2 = "MD2";
	public static final String MD4 = "MD4";
	public static final String MD5 = "MD5";

	public static final String SHA1 = "SHA-1";
	public static final String SHA256 = "SHA-256";

	public static final String HMAC_SHA1 = "HmacSHA1";

	public static final String RIPEMD128 = "RIPEMD128";
	public static final String RIPEMD160 = "RIPEMD160";

	/**
	 * 加密
	 * 
	 * @param source 被加密的字符串
	 * @param algorithmName 算法名
	 * @param charset 字符集
	 * @return 被加密后的值
	 */
	public static String encrypt(String source, String algorithmName, String charset) {
		return encrypt(StrUtil.bytes(source, charset), algorithmName);
	}

	/**
	 * 加密
	 * 
	 * @param bytes 被加密的byte数组
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static String encrypt(byte[] bytes, String algorithmName) {
		return Convert.toHex(encryptWithoutHex(bytes, algorithmName));
	}

	/**
	 * 加密，不对结果做Hex处理
	 * 
	 * @param bytes 被加密的byte数组
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static byte[] encryptWithoutHex(byte[] bytes, String algorithmName) {
		return createMessageDigest(algorithmName).digest(bytes);
	}
	
	/**
	 * 加密文件
	 * 
	 * @param file 被加密的文字
	 * @param algorithmName 算法名
	 * @return 被加密后的字符串
	 */
	public static String encrypt(File file, String algorithmName) {
		return Convert.toHex(encryptWithoutHex(file, algorithmName));
	}
	
	/**
	 * 加密文件，不对结果做Hex处理
	 * 
	 * @param file 被加密的文字
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static byte[] encryptWithoutHex(File file, String algorithmName) {
		final byte[] buffer = new byte[8192];
		MessageDigest md = createMessageDigest(algorithmName);
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			int length;
			while ((length = in.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
		} catch (IOException e) {
			throw new UtilException(e);
		}finally{
			IoUtil.close(in);
		}
		return md.digest();
	}
	
	/**
	 * 创建MessageDigest
	 * 
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static MessageDigest createMessageDigest(String algorithmName) {
		MessageDigest md = null;
		try {
			if (StrUtil.isBlank(algorithmName)) {
				algorithmName = MD5;
			}
			md = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(StrUtil.format("No such algorithm name for: {}", algorithmName));
		}
		return md;
	}

	/**
	 * SHA-1算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String sha1(String source, String charset) {
		return encrypt(source, SHA1, charset);
	}
	
	/**
	 * SHA-1算法加密
	 * 
	 * @param file 被加密的字符串
	 * @return 被加密后的字符串
	 */
	public static String sha1(File file) {
		return encrypt(file, SHA1);
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
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @return 被加密后的字符串
	 */
	public static String md5(byte[] source) {
		return encrypt(source, MD5);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param file 被加密的文件
	 * @return 被加密后的字符串
	 */
	public static String md5(File file) {
		return encrypt(file, MD5);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String md5(String source, String charset) {
		return encrypt(source, MD5, charset);
	}

	/**
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
