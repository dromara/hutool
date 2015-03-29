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
	
	/** base64码表 */
	private static char[] base64EncodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

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
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String sha1(String source, String charset) {
		return encrypt(source, SHA1, charset);
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
	 * base64编码
	 * 
	 * @param source 被编码的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String base64(String source, String charset) {
		return base64(StrUtil.encode(source, charset));
	}

	/**
	 * Base64的编码;
	 * 
	 * @param bytes 被编码的byte数组
	 * @return 编码后的值
	 */
	public static String base64(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		// 获取编码字节是3的倍数;
		int len = bytes.length;
		int len3 = len / 3;
		// 先处理没有加换行符;
		for (int i = 0; i < len3; i++) {

			// 得到第一个字符;
			int b1 = (bytes[i * 3] >> 2) & 0x3F;
			char c1 = base64EncodeTable[b1];
			sb.append(c1);

			// 得到第二个字符;
			int b2 = ((bytes[i * 3] << 4 & 0x3F) + (bytes[i * 3 + 1] >> 4)) & 0x3F;
			char c2 = base64EncodeTable[b2];
			sb.append(c2);

			// 得到第三个字符;
			int b3 = ((bytes[i * 3 + 1] << 2 & 0x3C) + (bytes[i * 3 + 2] >> 6)) & 0x3F;
			char c3 = base64EncodeTable[b3];
			sb.append(c3);

			// 得到第四个字符;
			int b4 = bytes[i * 3 + 2] & 0x3F;
			char c4 = base64EncodeTable[b4];
			sb.append(c4);

		}

		// 如果有剩余的字符就补0;
		// 剩余的个数;
		int less = len % 3;
		if (less == 1) {// 剩余一个字符--补充两个等号;;

			// 得到第一个字符;
			int b1 = bytes[len3 * 3] >> 2 & 0x3F;
			char c1 = base64EncodeTable[b1];
			sb.append(c1);

			// 得到第二个字符;
			int b2 = (bytes[len3 * 3] << 4 & 0x30) & 0x3F;
			char c2 = base64EncodeTable[b2];
			sb.append(c2);
			sb.append("==");

		} else if (less == 2) {// 剩余两个字符--补充一个等号;

			// 得到第一个字符;
			int b1 = bytes[len3 * 3] >> 2 & 0x3F;
			char c1 = base64EncodeTable[b1];
			sb.append(c1);

			// 得到第二个字符;
			int b2 = ((bytes[len3 * 3] << 4 & 0x30) + (bytes[len3 * 3 + 1] >> 4)) & 0x3F;
			char c2 = base64EncodeTable[b2];
			sb.append(c2);

			// 得到第三个字符;
			int b3 = (bytes[len3 * 3 + 1] << 2 & 0x3C) & 0x3F;
			char c3 = base64EncodeTable[b3];
			sb.append(c3);
			sb.append("=");

		}

		return sb.toString();
	}
	
	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeBase64(String source, String charset) {
		return decodeBase64(StrUtil.encode(source, charset));
	}

	/**
	 * Base64的解码;
	 * 
	 * @param bytes
	 * @return 解码后的字符串
	 */
	public static String decodeBase64(byte[] bytes) {

		// 每四个一组进行解码;
		int len = bytes.length;
		int len4 = len / 4;
		StringBuilder sb = new StringBuilder();
		// 除去末尾的四个可能特殊的字符;
		int i = 0;
		for (i = 0; i < len4 - 1; i++) {

			// 第一个字符;
			byte b1 = (byte) ((char2Index((char) bytes[i * 4]) << 2) + (char2Index((char) bytes[i * 4 + 1]) >> 4));
			sb.append((char) b1);
			// 第二个字符;
			byte b2 = (byte) ((char2Index((char) bytes[i * 4 + 1]) << 4) + (char2Index((char) bytes[i * 4 + 2]) >> 2));
			sb.append((char) b2);
			// 第三个字符;
			byte b3 = (byte) ((char2Index((char) bytes[i * 4 + 2]) << 6) + (char2Index((char) bytes[i * 4 + 3])));
			sb.append((char) b3);

		}

		// 处理最后的四个字符串;
		for (int j = 0; j < 3; j++) {
			int index = i * 4 + j;
			if ((char) bytes[index + 1] != '=') {

				if (j == 0) {
					byte b = (byte) ((char2Index((char) bytes[index]) << 2) + (char2Index((char) bytes[index + 1]) >> 4));
					sb.append((char) b);
				} else if (j == 1) {
					byte b = (byte) ((char2Index((char) bytes[index]) << 4) + (char2Index((char) bytes[index + 1]) >> 2));
					sb.append((char) b);
				} else if (j == 2) {
					byte b = (byte) ((char2Index((char) bytes[index]) << 6) + (char2Index((char) bytes[index + 1])));
					sb.append((char) b);
				}

			} else {
				break;
			}
		}

		return sb.toString();
	}

	/**
	 * 将码表中的字符映射到索引值;
	 * 
	 * @param ch 字符
	 * @return 索引值
	 */
	private static int char2Index(char ch) {
		if (ch >= 'A' && ch <= 'Z') {
			return ch - 'A';
		} else if (ch >= 'a' && ch <= 'z') {
			return 26 + ch - 'a';
		} else if (ch >= '0' && ch <= '9') {
			return 52 + ch - '0';
		} else if (ch == '+') {
			return 62;
		} else if (ch == '/') {
			return 63;
		}
		return 0;
	}
}
