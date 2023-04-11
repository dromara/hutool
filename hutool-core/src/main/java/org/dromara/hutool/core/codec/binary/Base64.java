/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.codec.binary;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Base64工具类，提供Base64的编码和解码方案<br>
 * base64编码是用64（2的6次方）个ASCII字符来表示256（2的8次方）个ASCII字符，<br>
 * 也就是三位二进制数组经过编码后变为四位的ASCII字符显示，长度比原来增加1/3。
 *
 * @author Looly
 */
public class Base64 {

	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
	// -------------------------------------------------------------------- encode

	/**
	 * 编码为Base64，非URL安全的
	 *
	 * @param arr     被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 */
	public static byte[] encode(final byte[] arr, final boolean lineSep) {
		return lineSep ?
				java.util.Base64.getMimeEncoder().encode(arr) :
				java.util.Base64.getEncoder().encode(arr);
	}

	/**
	 * base64编码
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(final CharSequence source) {
		return encode(source, DEFAULT_CHARSET);
	}

	/**
	 * base64编码，URL安全
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(final CharSequence source) {
		return encodeUrlSafe(source, DEFAULT_CHARSET);
	}

	/**
	 * base64编码
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被编码后的字符串
	 */
	public static String encode(final CharSequence source, final Charset charset) {
		return encode(ByteUtil.toBytes(source, charset));
	}

	/**
	 * base64编码，URL安全的
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(final CharSequence source, final Charset charset) {
		return encodeUrlSafe(ByteUtil.toBytes(source, charset));
	}

	/**
	 * base64编码
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(final byte[] source) {
		return java.util.Base64.getEncoder().encodeToString(source);
	}

	/**
	 * base64编码，不进行padding(末尾不会填充'=')
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * @since 5.5.2
	 */
	public static String encodeWithoutPadding(final byte[] source) {
		return java.util.Base64.getEncoder().withoutPadding().encodeToString(source);
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(final byte[] source) {
		return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(source);
	}

	/**
	 * base64编码
	 *
	 * @param in 被编码base64的流（一般为图片流或者文件流）
	 * @return 被加密后的字符串
	 * @since 4.0.9
	 */
	public static String encode(final InputStream in) {
		return encode(IoUtil.readBytes(in));
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param in 被编码base64的流（一般为图片流或者文件流）
	 * @return 被加密后的字符串
	 * @since 4.0.9
	 */
	public static String encodeUrlSafe(final InputStream in) {
		return encodeUrlSafe(IoUtil.readBytes(in));
	}

	/**
	 * base64编码
	 *
	 * @param file 被编码base64的文件
	 * @return 被加密后的字符串
	 * @since 4.0.9
	 */
	public static String encode(final File file) {
		return encode(FileUtil.readBytes(file));
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param file 被编码base64的文件
	 * @return 被加密后的字符串
	 * @since 4.0.9
	 */
	public static String encodeUrlSafe(final File file) {
		return encodeUrlSafe(FileUtil.readBytes(file));
	}

	// -------------------------------------------------------------------- decode

	/**
	 * base64解码
	 *
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 * @since 4.3.2
	 */
	public static String decodeStrGbk(final CharSequence source) {
		return decodeStr(source, CharsetUtil.GBK);
	}

	/**
	 * base64解码
	 *
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(final CharSequence source) {
		return decodeStr(source, DEFAULT_CHARSET);
	}

	/**
	 * base64解码
	 *
	 * @param source  被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(final CharSequence source, final Charset charset) {
		return StrUtil.str(decode(source), charset);
	}

	/**
	 * base64解码
	 *
	 * @param base64   被解码的base64字符串
	 * @param destFile 目标文件
	 * @return 目标文件
	 * @since 4.0.9
	 */
	public static File decodeToFile(final CharSequence base64, final File destFile) {
		return FileUtil.writeBytes(decode(base64), destFile);
	}

	/**
	 * base64解码
	 *
	 * @param base64     被解码的base64字符串
	 * @param out        写出到的流
	 * @param isCloseOut 是否关闭输出流
	 * @since 4.0.9
	 */
	public static void decodeToStream(final CharSequence base64, final OutputStream out, final boolean isCloseOut) {
		IoUtil.write(out, isCloseOut, decode(base64));
	}

	/**
	 * base64解码
	 *
	 * @param base64 被解码的base64字符串
	 * @return 解码后的bytes
	 */
	public static byte[] decode(final CharSequence base64) {
		return decode(ByteUtil.toBytes(base64, DEFAULT_CHARSET));
	}

	/**
	 * 解码Base64
	 *
	 * @param in 输入
	 * @return 解码后的bytes
	 */
	public static byte[] decode(final byte[] in) {
		return Base64Decoder.INSTANCE.decode(in);
	}

	/**
	 * 检查是否为Base64
	 *
	 * @param base64 Base64的bytes
	 * @return 是否为Base64
	 * @since 5.7.5
	 */
	public static boolean isBase64(final CharSequence base64) {
		if (base64 == null || base64.length() < 2) {
			return false;
		}

		final byte[] bytes = ByteUtil.toUtf8Bytes(base64);

		if (bytes.length != base64.length()) {
			// 如果长度不相等，说明存在双字节字符，肯定不是Base64，直接返回false
			return false;
		}

		return isBase64(bytes);
	}

	/**
	 * 检查是否为Base64
	 *
	 * @param base64Bytes Base64的bytes
	 * @return 是否为Base64
	 * @since 5.7.5
	 */
	public static boolean isBase64(final byte[] base64Bytes) {
		if (base64Bytes == null || base64Bytes.length < 3) {
			return false;
		}

		boolean hasPadding = false;
		for (final byte base64Byte : base64Bytes) {
			if (hasPadding) {
				if ('=' != base64Byte) {
					// 前一个字符是'='，则后边的字符都必须是'='，即'='只能都位于结尾
					return false;
				}
			} else if ('=' == base64Byte) {
				// 发现'=' 标记之
				hasPadding = true;
			} else if (!(Base64Decoder.INSTANCE.isBase64Code(base64Byte) || isWhiteSpace(base64Byte))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isWhiteSpace(final byte byteToCheck) {
		switch (byteToCheck) {
			case ' ':
			case '\n':
			case '\r':
			case '\t':
				return true;
			default:
				return false;
		}
	}
}
