package com.xiaoleilu.hutool.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * 摘要算法工具类
 * 
 * @author Looly
 *
 */
public class DigestUtil {

	/**
	 * 摘要算法类型
	 * 
	 * @author Looly
	 *
	 */
	public static enum Algorithm {
		MD5("MD5"), SHA256("SHA-256"), SHA348("SHA-348"), SHA512("SHA-512"), SHA1("SHA-1");

		private String value;

		private Algorithm(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	// ------------------------------------------------------------------------------------------- MD5
	/**
	 * 计算16位MD5摘要值
	 * 
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(byte[] data) {
		return getDigest(Algorithm.MD5).digest(data);
	}
	
	/**
	 * 计算16位MD5摘要值
	 * 
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return MD5摘要
	 */
	public static byte[] md5(String data, String charset) {
		return md5(StrUtil.bytes(data, charset));
	}
	
	/**
	 * 计算16位MD5摘要值，使用UTF-8编码
	 * 
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(String data) {
		return md5(data, CharsetUtil.UTF_8);
	}
	
	/**
	 * 计算16位MD5摘要值
	 * 
	 * @param data 被摘要数据
	 * @return MD5摘要
	 * @throws UtilException Cause by IOException
	 */
	public static byte[] md5(InputStream data) {
		try {
			return digest(getDigest(Algorithm.MD5), data);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 计算16位MD5摘要值
	 * 
	 * @param file 被摘要文件
	 * @return MD5摘要
	 * @throws UtilException Cause by IOException
	 */
	public static byte[] md5(File file) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return digest(getDigest(Algorithm.MD5), in);
		} catch (IOException e) {
			throw new UtilException(e);
		}finally{
			IoUtil.close(in);
		}
	}
	
	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @return  MD5摘要的16进制表示
	 */
	public static String md5Hex(byte[] data){
		return HexUtil.encodeHexStr(md5(data));
	}
	
	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return  MD5摘要的16进制表示
	 */
	public static String md5Hex(String data, String charset){
		return HexUtil.encodeHexStr(md5(data, charset));
	}
	
	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @return  MD5摘要的16进制表示
	 */
	public static String md5Hex(String data){
		return md5Hex(data, CharsetUtil.UTF_8);
	}
	
	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return  MD5摘要的16进制表示
	 */
	public static String md5Hex(InputStream data){
		return HexUtil.encodeHexStr(md5(data));
	}
	
	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 * @param file 被摘要文件
	 * @param charset 编码
	 * @return  MD5摘要的16进制表示
	 */
	public static String md5Hex(File file){
		return HexUtil.encodeHexStr(md5(file));
	}
	
	// ------------------------------------------------------------------------------------------- SHA-1
	/**
	 * 计算SHA-1摘要值
	 * 
	 * @param data 被摘要数据
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(byte[] data) {
		return getDigest(Algorithm.SHA1).digest(data);
	}
	
	/**
	 * 计算SHA-1摘要值
	 * 
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(String data, String charset) {
		return sha1(StrUtil.bytes(data, charset));
	}
	
	/**
	 * 计算sha1摘要值，使用UTF-8编码
	 * 
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] sha1(String data) {
		return sha1(data, CharsetUtil.UTF_8);
	}
	
	/**
	 * 计算SHA-1摘要值
	 * 
	 * @param data 被摘要数据
	 * @return SHA-1摘要
	 * @throws UtilException Cause by IOException
	 */
	public static byte[] sha1(InputStream data) {
		try {
			return digest(getDigest(Algorithm.SHA1), data);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 计算SHA-1摘要值
	 * 
	 * @param file 被摘要文件
	 * @return SHA-1摘要
	 * @throws UtilException Cause by IOException
	 */
	public static byte[] sha1(File file) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return digest(getDigest(Algorithm.SHA1), in);
		} catch (IOException e) {
			throw new UtilException(e);
		}finally{
			IoUtil.close(in);
		}
	}
	
	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @return  SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(byte[] data){
		return HexUtil.encodeHexStr(sha1(data));
	}
	
	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return  SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(String data, String charset){
		return HexUtil.encodeHexStr(sha1(data, charset));
	}
	
	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @return  SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(String data){
		return sha1Hex(data, CharsetUtil.UTF_8);
	}
	
	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return  SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(InputStream data){
		return HexUtil.encodeHexStr(sha1(data));
	}
	
	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 * @param file 被摘要文件
	 * @param charset 编码
	 * @return  SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(File file){
		return HexUtil.encodeHexStr(sha1(file));
	}
	
	// ------------------------------------------------------------------------------------------- Digest
	/**
	 * 生成摘要，使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param digest {@link MessageDigest}
	 * @param data {@link InputStream} 数据流
	 * @return 摘要bytes
	 * @throws IOException 流中读取数据产生的异常
	 */
	public static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
		return digest(digest, data, IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 生成摘要
	 * 
	 * @param digest {@link MessageDigest}
	 * @param data {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要bytes
	 * @throws IOException 流中读取数据产生的异常
	 */
	public static byte[] digest(MessageDigest digest, InputStream data, int bufferLength) throws IOException {
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		byte[] buffer = new byte[bufferLength];
		int read = data.read(buffer, 0, bufferLength);

		while (read > -1) {
			digest.update(buffer, 0, read);
			read = data.read(buffer, 0, bufferLength);
		}

		return digest.digest();
	}
	
	/**
	 * 创建 {@link MessageDigest}
	 * 
	 * @param algorithm 摘要算法类型枚举 {@link Algorithm}
	 * @return 被加密后的值
	 */
	public static MessageDigest getDigest(Algorithm algorithm) {
		return getDigest(algorithm.getValue());
	}

	/**
	 * 创建 {@link MessageDigest}
	 * 
	 * @param algorithmName 算法名
	 * @return 被加密后的值
	 */
	public static MessageDigest getDigest(String algorithmName) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(e);
		}
		return md;
	}
}
