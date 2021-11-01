package cn.hutool.crypto.digest;

import cn.hutool.core.util.CharsetUtil;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 摘要算法工具类
 *
 * @author Looly
 */
public class DigestUtil {

	// ------------------------------------------------------------------------------------------- MD5

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(byte[] data) {
		return new MD5().digest(data);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要
	 */
	public static byte[] md5(String data, String charset) {
		return new MD5().digest(data, charset);
	}

	/**
	 * 计算32位MD5摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(String data) {
		return md5(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(InputStream data) {
		return new MD5().digest(data);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要
	 */
	public static byte[] md5(File file) {
		return new MD5().digest(file);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(byte[] data) {
		return new MD5().digestHex(data);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(String data, String charset) {
		return new MD5().digestHex(data, charset);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex(String data, Charset charset) {
		return new MD5().digestHex(data, charset);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(String data) {
		return md5Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(InputStream data) {
		return new MD5().digestHex(data);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(File file) {
		return new MD5().digestHex(file);
	}

	// ------------------------------------------------------------------------------------------- MD5 16

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(byte[] data) {
		return new MD5().digestHex16(data);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(String data, Charset charset) {
		return new MD5().digestHex16(data, charset);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(String data) {
		return md5Hex16(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(InputStream data) {
		return new MD5().digestHex16(data);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(File file) {
		return new MD5().digestHex16(file);
	}

	/**
	 * 32位MD5转16位MD5
	 *
	 * @param md5Hex 32位MD5
	 * @return 16位MD5
	 * @since 4.4.1
	 */
	public static String md5HexTo16(String md5Hex) {
		return md5Hex.substring(8, 24);
	}

	// ------------------------------------------------------------------------------------------- SHA-1

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(byte[] data) {
		return new Digester(DigestAlgorithm.SHA1).digest(data);
	}

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(String data, String charset) {
		return new Digester(DigestAlgorithm.SHA1).digest(data, charset);
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
	 */
	public static byte[] sha1(InputStream data) {
		return new Digester(DigestAlgorithm.SHA1).digest(data);
	}

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param file 被摘要文件
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(File file) {
		return new Digester(DigestAlgorithm.SHA1).digest(file);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(byte[] data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(String data, String charset) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data, charset);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(String data) {
		return sha1Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(InputStream data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(File file) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(file);
	}

	// ------------------------------------------------------------------------------------------- SHA-256

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(byte[] data) {
		return new Digester(DigestAlgorithm.SHA256).digest(data);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(String data, String charset) {
		return new Digester(DigestAlgorithm.SHA256).digest(data, charset);
	}

	/**
	 * 计算sha256摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(String data) {
		return sha256(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(InputStream data) {
		return new Digester(DigestAlgorithm.SHA256).digest(data);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param file 被摘要文件
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(File file) {
		return new Digester(DigestAlgorithm.SHA256).digest(file);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(byte[] data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(String data, String charset) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data, charset);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(String data) {
		return sha256Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(InputStream data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(File file) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(file);
	}

	// ------------------------------------------------------------------------------------------- Hmac

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(HmacAlgorithm algorithm, byte[] key) {
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
	public static HMac hmac(HmacAlgorithm algorithm, SecretKey key) {
		return new HMac(algorithm, key);
	}

	/**
	 * 新建摘要器
	 *
	 * @param algorithm 签名算法
	 * @return Digester
	 * @since 4.0.1
	 */
	public static Digester digester(DigestAlgorithm algorithm) {
		return new Digester(algorithm);
	}

	/**
	 * 新建摘要器
	 *
	 * @param algorithm 签名算法
	 * @return Digester
	 * @since 4.2.1
	 */
	public static Digester digester(String algorithm) {
		return new Digester(algorithm);
	}

	/**
	 * 生成Bcrypt加密后的密文
	 *
	 * @param password 明文密码
	 * @return 加密后的密文
	 * @since 4.1.1
	 */
	public static String bcrypt(String password) {
		return BCrypt.hashpw(password);
	}

	/**
	 * 验证密码是否与Bcrypt加密后的密文匹配
	 *
	 * @param password 明文密码
	 * @param hashed   hash值（加密后的值）
	 * @return 是否匹配
	 * @since 4.1.1
	 */
	public static boolean bcryptCheck(String password, String hashed) {
		return BCrypt.checkpw(password, hashed);
	}
}
