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

package org.dromara.hutool.crypto.digest;

import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.crypto.digest.mac.HMac;
import org.dromara.hutool.crypto.digest.mac.HmacAlgorithm;

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

	// region ----- MD5

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(final byte[] data) {
		return MD5.of().digest(data);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要
	 */
	public static byte[] md5(final String data, final Charset charset) {
		return MD5.of().digest(data, charset);
	}

	/**
	 * 计算32位MD5摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(final String data) {
		return md5(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] md5(final InputStream data) {
		return MD5.of().digest(data);
	}

	/**
	 * 计算32位MD5摘要值
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要
	 */
	public static byte[] md5(final File file) {
		return MD5.of().digest(file);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(final byte[] data) {
		return MD5.of().digestHex(data);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex(final String data, final Charset charset) {
		return MD5.of().digestHex(data, charset);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(final String data) {
		return md5Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(final InputStream data) {
		return MD5.of().digestHex(data);
	}

	/**
	 * 计算32位MD5摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要的16进制表示
	 */
	public static String md5Hex(final File file) {
		return MD5.of().digestHex(file);
	}

	// endregion

	// region ----- MD5 16

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(final byte[] data) {
		return MD5.of().digestHex16(data);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(final String data, final Charset charset) {
		return MD5.of().digestHex16(data, charset);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(final String data) {
		return md5Hex16(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(final InputStream data) {
		return MD5.of().digestHex16(data);
	}

	/**
	 * 计算16位MD5摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return MD5摘要的16进制表示
	 * @since 4.6.0
	 */
	public static String md5Hex16(final File file) {
		return MD5.of().digestHex16(file);
	}

	/**
	 * 32位MD5转16位MD5
	 *
	 * @param md5Hex 32位MD5
	 * @return 16位MD5
	 * @since 4.4.1
	 */
	public static String md5HexTo16(final String md5Hex) {
		return md5Hex.substring(8, 24);
	}

	// endregion

	// region ----- SHA-1

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(final byte[] data) {
		return digester(DigestAlgorithm.SHA1).digest(data);
	}

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA1).digest(data, charset);
	}

	/**
	 * 计算sha1摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] sha1(final String data) {
		return sha1(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(final InputStream data) {
		return digester(DigestAlgorithm.SHA1).digest(data);
	}

	/**
	 * 计算SHA-1摘要值
	 *
	 * @param file 被摘要文件
	 * @return SHA-1摘要
	 */
	public static byte[] sha1(final File file) {
		return digester(DigestAlgorithm.SHA1).digest(file);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(final byte[] data) {
		return digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA1).digestHex(data, charset);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(final String data) {
		return sha1Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(final InputStream data) {
		return digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return SHA-1摘要的16进制表示
	 */
	public static String sha1Hex(final File file) {
		return digester(DigestAlgorithm.SHA1).digestHex(file);
	}

	// endregion

	// region ----- SHA-256

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(final byte[] data) {
		return digester(DigestAlgorithm.SHA256).digest(data);
	}

	/**
	 * 计算sha256摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(final String data) {
		return sha256(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-256摘要
	 */
	public static byte[] sha256(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA256).digest(data, charset);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(final InputStream data) {
		return digester(DigestAlgorithm.SHA256).digest(data);
	}

	/**
	 * 计算SHA-256摘要值
	 *
	 * @param file 被摘要文件
	 * @return SHA-256摘要
	 * @since 3.0.8
	 */
	public static byte[] sha256(final File file) {
		return digester(DigestAlgorithm.SHA256).digest(file);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(final byte[] data) {
		return digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA256).digestHex(data, charset);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(final String data) {
		return sha256Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(final InputStream data) {
		return digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * 计算SHA-256摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return SHA-256摘要的16进制表示
	 * @since 3.0.8
	 */
	public static String sha256Hex(final File file) {
		return digester(DigestAlgorithm.SHA256).digestHex(file);
	}

	// endregion

	// region ----- SHA-512

	/**
	 * 计算SHA-512摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-512摘要
	 */
	public static byte[] sha512(final byte[] data) {
		return digester(DigestAlgorithm.SHA512).digest(data);
	}

	/**
	 * 计算SHA-512摘要值
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-512摘要
	 * @since 3.0.8
	 */
	public static byte[] sha512(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA512).digest(data, charset);
	}

	/**
	 * 计算sha512摘要值，使用UTF-8编码
	 *
	 * @param data 被摘要数据
	 * @return MD5摘要
	 */
	public static byte[] sha512(final String data) {
		return sha512(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-512摘要值
	 *
	 * @param data 被摘要数据
	 * @return SHA-512摘要
	 */
	public static byte[] sha512(final InputStream data) {
		return digester(DigestAlgorithm.SHA512).digest(data);
	}

	/**
	 * 计算SHA-512摘要值
	 *
	 * @param file 被摘要文件
	 * @return SHA-512摘要
	 */
	public static byte[] sha512(final File file) {
		return digester(DigestAlgorithm.SHA512).digest(file);
	}

	/**
	 * 计算SHA-1摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-512摘要的16进制表示
	 */
	public static String sha512Hex(final byte[] data) {
		return digester(DigestAlgorithm.SHA512).digestHex(data);
	}

	/**
	 * 计算SHA-512摘要值，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return SHA-512摘要的16进制表示
	 */
	public static String sha512Hex(final String data, final Charset charset) {
		return digester(DigestAlgorithm.SHA512).digestHex(data, charset);
	}

	/**
	 * 计算SHA-512摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-512摘要的16进制表示
	 */
	public static String sha512Hex(final String data) {
		return sha512Hex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 计算SHA-512摘要值，并转为16进制字符串
	 *
	 * @param data 被摘要数据
	 * @return SHA-512摘要的16进制表示
	 */
	public static String sha512Hex(final InputStream data) {
		return digester(DigestAlgorithm.SHA512).digestHex(data);
	}

	/**
	 * 计算SHA-512摘要值，并转为16进制字符串
	 *
	 * @param file 被摘要文件
	 * @return SHA-512摘要的16进制表示
	 */
	public static String sha512Hex(final File file) {
		return digester(DigestAlgorithm.SHA512).digestHex(file);
	}

	// endregion

	// region ----- Hmac

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

	// endregion

	/**
	 * 新建摘要器
	 *
	 * @param algorithm 签名算法
	 * @return Digester
	 * @since 4.0.1
	 */
	public static Digester digester(final DigestAlgorithm algorithm) {
		return digester(algorithm.getValue());
	}

	/**
	 * 新建摘要器
	 *
	 * @param algorithm 签名算法
	 * @return Digester
	 * @since 4.2.1
	 */
	public static Digester digester(final String algorithm) {
		return new Digester(algorithm);
	}

	/**
	 * 生成Bcrypt加密后的密文
	 *
	 * @param password 明文密码
	 * @return 加密后的密文
	 * @since 4.1.1
	 */
	public static String bcrypt(final String password) {
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
	public static boolean bcryptCheck(final String password, final String hashed) {
		return BCrypt.checkpw(password, hashed);
	}
}
