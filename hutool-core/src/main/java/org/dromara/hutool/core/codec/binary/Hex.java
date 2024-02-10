/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.codec.binary;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。<br>
 * 例如十进制数57，在二进制写作111001，在16进制写作39。<br>
 * 像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20<br>
 * <p>
 * 参考：<a href="https://my.oschina.net/xinxingegeya/blog/287476">https://my.oschina.net/xinxingegeya/blog/287476</a>
 *
 * @author Looly
 * @see Base16Codec
 */
public class Hex {
	// region ----- encode

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data byte[]
	 * @return 十六进制char[]
	 */
	public static char[] encode(final byte[] data) {
		return encode(data, true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param str     字符串
	 * @param charset 编码
	 * @return 十六进制char[]
	 */
	public static char[] encode(final String str, final Charset charset) {
		return encode(ByteUtil.toBytes(str, charset), true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data        byte[]
	 * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
	 * @return 十六进制char[]。如果提供的data为{@code null}，返回{@code null}
	 */
	public static char[] encode(final byte[] data, final boolean toLowerCase) {
		if (null == data) {
			return null;
		}
		return (toLowerCase ? Base16Codec.CODEC_LOWER : Base16Codec.CODEC_UPPER).encode(data);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data byte[]
	 * @return 十六进制String
	 */
	public static String encodeStr(final byte[] data) {
		return encodeStr(data, true);
	}

	/**
	 * 将字符串转换为十六进制字符串，结果为小写，默认编码是UTF-8
	 *
	 * @param data 被编码的字符串
	 * @return 十六进制String
	 */
	public static String encodeStr(final String data) {
		return encodeStr(data, CharsetUtil.UTF_8);
	}

	/**
	 * 将字符串转换为十六进制字符串，结果为小写
	 *
	 * @param data    需要被编码的字符串
	 * @param charset 编码
	 * @return 十六进制String
	 */
	public static String encodeStr(final String data, final Charset charset) {
		return encodeStr(ByteUtil.toBytes(data, charset), true);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data        byte[]
	 * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
	 * @return 十六进制String
	 */
	public static String encodeStr(final byte[] data, final boolean toLowerCase) {
		return StrUtil.str(encode(data, toLowerCase), CharsetUtil.UTF_8);
	}
	// endregion

	// region ----- decode

	/**
	 * 将十六进制字符数组转换为字符串，默认编码UTF-8
	 *
	 * @param hexStr 十六进制String
	 * @return 字符串
	 */
	public static String decodeStr(final String hexStr) {
		return decodeStr(hexStr, CharsetUtil.UTF_8);
	}

	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexStr  十六进制String
	 * @param charset 编码
	 * @return 字符串
	 */
	public static String decodeStr(final String hexStr, final Charset charset) {
		if (StrUtil.isEmpty(hexStr)) {
			return hexStr;
		}
		return StrUtil.str(decode(hexStr), charset);
	}

	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexData 十六进制char[]
	 * @param charset 编码
	 * @return 字符串
	 */
	public static String decodeStr(final char[] hexData, final Charset charset) {
		return StrUtil.str(decode(hexData), charset);
	}

	/**
	 * 将十六进制字符串解码为byte[]
	 *
	 * @param hexStr 十六进制String
	 * @return byte[]
	 */
	public static byte[] decode(final String hexStr) {
		return decode((CharSequence) hexStr);
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 *
	 * @param hexData 十六进制char[]
	 * @return byte[]
	 * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
	 */
	public static byte[] decode(final char[] hexData) {
		return decode(String.valueOf(hexData));
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 *
	 * @param hexData 十六进制字符串
	 * @return byte[]
	 * @throws HutoolException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
	 * @since 5.6.6
	 */
	public static byte[] decode(final CharSequence hexData) {
		return Base16Codec.CODEC_LOWER.decode(hexData);
	}

	// endregion
}
