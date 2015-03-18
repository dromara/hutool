package com.xiaoleilu.hutool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 安全相关工具类
 * 
 * @author xiaoleilu
 *
 */
public class SecureUtil {

	public final static String MD2 = "MD2";
	public final static String MD4 = "MD4";
	public final static String MD5 = "MD5";
	
	public final static String SHA1 = "SHA-1";
	public final static String SHA256 = "SHA-256";
	
	public final static String RIPEMD128 = "RIPEMD128";
	public final static String RIPEMD160 = "RIPEMD160";

	/**
	 * 加密
	 * 
	 * @param source 被加密的字符串
	 * @param algorithmName 算法名
	 * @param charset 字符集
	 * @return 被加密后的值
	 */
	public static String encrypt(String source, String algorithmName, String charset) {
		return encrypt(StrUtil.encode(source, charset), algorithmName);
	}
	
	/**
	 * 加密
	 * 
	 * @param bytes 被加密的byte数组
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static String encrypt(byte[] bytes, String algorithmName) {
		return Conver.toHex(encryptWithoutHex(bytes, algorithmName));
	}
	
	/**
	 * 加密，不对结果做Hex处理
	 * 
	 * @param bytes 被加密的byte数组
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static byte[] encryptWithoutHex(byte[] bytes, String algorithmName) {
		MessageDigest md = null;
		try {
			if (StrUtil.isBlank(algorithmName)) {
				algorithmName = MD5;
			}
			md = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(StrUtil.format("No such algorithm name for: {}", algorithmName));
		}
		return md.digest(bytes);
	}
	
	/**
	 * SHA-1算法加密
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String sha1(String source, String charset){
		return encrypt(source, SHA1, charset);
	}
	
	/**
	 * MD5算法加密
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String md5(String source, String charset){
		return encrypt(source, MD5, charset);
	}
	
}
