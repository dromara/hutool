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

package org.dromara.hutool.core.codec.binary;

import org.dromara.hutool.core.codec.Decoder;
import org.dromara.hutool.core.lang.mutable.MutableInt;
import org.dromara.hutool.core.array.ArrayUtil;

import java.io.Serializable;

/**
 * Base64解码实现<br>
 * 此解码保留的原因是，JDK提供的解码需要指定是否为URL安全的或是否换行，此解码无需区分
 *
 * @author looly
 *
 */
public class Base64Decoder implements Decoder<byte[], byte[]>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static Base64Decoder INSTANCE = new Base64Decoder();

	private static final byte PADDING = -2;

	/** Base64解码表，共128位，-1表示非base64字符，-2表示padding */
	private final byte[] DECODE_TABLE = {
			// 0 1 2 3 4 5 6 7 8 9 A B C D E F
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 00-0f
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 10-1f
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, // 20-2f + - /
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, // 30-3f 0-9，-2的位置是'='
			-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // 40-4f A-O
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, // 50-5f P-Z _
			-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, // 60-6f a-o
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 // 70-7a p-z
	};

	/**
	 * 解码Base64
	 *
	 * @param in 输入
	 * @return 解码后的bytes
	 */
	@Override
	public byte[] decode(final byte[] in) {
		if (ArrayUtil.isEmpty(in)) {
			return in;
		}
		return decode(in, 0, in.length);
	}

	/**
	 * 解码Base64
	 *
	 * @param in 输入
	 * @param pos 开始位置
	 * @param length 长度
	 * @return 解码后的bytes
	 */
	public byte[] decode(final byte[] in, final int pos, final int length) {
		if (ArrayUtil.isEmpty(in)) {
			return in;
		}

		final MutableInt offset = new MutableInt(pos);

		byte sestet0;
		byte sestet1;
		byte sestet2;
		byte sestet3;
		final int maxPos = pos + length - 1;
		int octetId = 0;
		final byte[] octet = new byte[length * 3 / 4];// over-estimated if non-base64 characters present
		while (offset.intValue() <= maxPos) {
			sestet0 = getNextValidDecodeByte(in, offset, maxPos);
			sestet1 = getNextValidDecodeByte(in, offset, maxPos);
			sestet2 = getNextValidDecodeByte(in, offset, maxPos);
			sestet3 = getNextValidDecodeByte(in, offset, maxPos);

			if (PADDING != sestet1) {
				octet[octetId++] = (byte) ((sestet0 << 2) | (sestet1 >>> 4));
			}
			if (PADDING != sestet2) {
				octet[octetId++] = (byte) (((sestet1 & 0xf) << 4) | (sestet2 >>> 2));
			}
			if (PADDING != sestet3) {
				octet[octetId++] = (byte) (((sestet2 & 3) << 6) | sestet3);
			}
		}

		if (octetId == octet.length) {
			return octet;
		} else {
			// 如果有非Base64字符混入，则实际结果比解析的要短，截取之
			return ArrayUtil.copy(octet, new byte[octetId], octetId);
		}
	}

	/**
	 * 给定的字符是否为Base64字符
	 *
	 * @param octet 被检查的字符
	 * @return 是否为Base64字符
	 * @since 5.7.5
	 */
	public boolean isBase64Code(final byte octet) {
		return octet == '=' || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
	}

	// ----------------------------------------------------------------------------------------------- Private start
	/**
	 * 获取下一个有效的byte字符
	 *
	 * @param in 输入
	 * @param pos 当前位置，调用此方法后此位置保持在有效字符的下一个位置
	 * @param maxPos 最大位置
	 * @return 有效字符，如果达到末尾返回
	 */
	private byte getNextValidDecodeByte(final byte[] in, final MutableInt pos, final int maxPos) {
		byte base64Byte;
		byte decodeByte;
		while (pos.intValue() <= maxPos) {
			base64Byte = in[pos.intValue()];
			pos.increment();
			if (base64Byte > -1) {
				decodeByte = DECODE_TABLE[base64Byte];
				if (decodeByte > -1) {
					return decodeByte;
				}
			}
		}
		// padding if reached max position
		return PADDING;
	}
	// ----------------------------------------------------------------------------------------------- Private end
}
