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
import org.dromara.hutool.core.codec.Encoder;

import java.util.Arrays;

/**
 * Base32 - encodes and decodes RFC4648 Base32<br>
 * (see <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-6">https://datatracker.ietf.org/doc/html/rfc4648#section-6</a> )<br>
 * base32就是用32（2的5次方）个特定ASCII码来表示256个ASCII码。<br>
 * 所以，5个ASCII字符经过base32编码后会变为8个字符（公约数为40），长度增加3/5.不足8n用“=”补足。<br>
 * 根据RFC4648 Base32规范，支持两种模式：
 * <ul>
 *     <li>Base 32 Alphabet                 (ABCDEFGHIJKLMNOPQRSTUVWXYZ234567)</li>
 *     <li>"Extended Hex" Base 32 Alphabet  (0123456789ABCDEFGHIJKLMNOPQRSTUV)</li>
 * </ul>
 *
 * @author Looly
 * @since 5.8.0
 */
public class Base32Codec implements Encoder<byte[], String>, Decoder<CharSequence, byte[]> {

	/**
	 * 单例对象
	 */
	public static Base32Codec INSTANCE = new Base32Codec();

	@Override
	public String encode(final byte[] data) {
		return encode(data, false);
	}

	/**
	 * 编码数据
	 *
	 * @param data   数据
	 * @param useHex 是否使用Hex Alphabet
	 * @return 编码后的Base32字符串
	 */
	public String encode(final byte[] data, final boolean useHex) {
		final Base32Encoder encoder = useHex ? Base32Encoder.HEX_ENCODER : Base32Encoder.ENCODER;
		return encoder.encode(data);
	}

	@Override
	public byte[] decode(final CharSequence encoded) {
		return decode(encoded, false);
	}

	/**
	 * 解码数据
	 *
	 * @param encoded base32字符串
	 * @param useHex  是否使用Hex Alphabet
	 * @return 解码后的内容
	 */
	public byte[] decode(final CharSequence encoded, final boolean useHex) {
		final Base32Decoder decoder = useHex ? Base32Decoder.HEX_DECODER : Base32Decoder.DECODER;
		return decoder.decode(encoded);
	}

	/**
	 * Bas32编码器
	 */
	public static class Base32Encoder implements Encoder<byte[], String> {
		private static final String DEFAULT_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
		private static final String HEX_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUV";
		private static final Character DEFAULT_PAD = '=';
		private static final int[] BASE32_FILL = {-1, 4, 1, 6, 3};

		/**
		 * 编码器
		 */
		public static final Base32Encoder ENCODER = new Base32Encoder(DEFAULT_ALPHABET, DEFAULT_PAD);
		/**
		 * 16进制编码器
		 */
		public static final Base32Encoder HEX_ENCODER = new Base32Encoder(HEX_ALPHABET, DEFAULT_PAD);

		private final char[] alphabet;
		private final Character pad;

		/**
		 * 构造
		 *
		 * @param alphabet 自定义编码字母表，见 {@link #DEFAULT_ALPHABET}和 {@link #HEX_ALPHABET}
		 * @param pad      补位字符
		 */
		public Base32Encoder(final String alphabet, final Character pad) {
			this.alphabet = alphabet.toCharArray();
			this.pad = pad;
		}

		@Override
		public String encode(final byte[] data) {
			int i = 0;
			int index = 0;
			int digit;
			int currByte;
			int nextByte;

			int encodeLen = data.length * 8 / 5;
			if (encodeLen != 0) {
				encodeLen = encodeLen + 1 + BASE32_FILL[(data.length * 8) % 5];
			}

			final StringBuilder base32 = new StringBuilder(encodeLen);

			while (i < data.length) {
				// unsign
				currByte = (data[i] >= 0) ? data[i] : (data[i] + 256);

				/* Is the current digit going to span a byte boundary? */
				if (index > 3) {
					if ((i + 1) < data.length) {
						nextByte = (data[i + 1] >= 0) ? data[i + 1] : (data[i + 1] + 256);
					} else {
						nextByte = 0;
					}

					digit = currByte & (0xFF >> index);
					index = (index + 5) % 8;
					digit <<= index;
					digit |= nextByte >> (8 - index);
					i++;
				} else {
					digit = (currByte >> (8 - (index + 5))) & 0x1F;
					index = (index + 5) % 8;
					if (index == 0) {
						i++;
					}
				}
				base32.append(alphabet[digit]);
			}

			if (null != pad) {
				// 末尾补充不足长度的
				while (base32.length() < encodeLen) {
					base32.append(pad.charValue());
				}
			}

			return base32.toString();
		}
	}

	/**
	 * Base32解码器
	 */
	public static class Base32Decoder implements Decoder<CharSequence, byte[]> {
		private static final char BASE_CHAR = '0';

		/**
		 * 解码器
		 */
		public static final Base32Decoder DECODER = new Base32Decoder(Base32Encoder.DEFAULT_ALPHABET);
		/**
		 * 16进制解码器
		 */
		public static final Base32Decoder HEX_DECODER = new Base32Decoder(Base32Encoder.HEX_ALPHABET);

		private final byte[] lookupTable;

		/**
		 * 构造
		 *
		 * @param alphabet 编码字母表
		 */
		public Base32Decoder(final String alphabet) {
			lookupTable = new byte[128];
			Arrays.fill(lookupTable, (byte) -1);

			final int length = alphabet.length();

			char c;
			for (int i = 0; i < length; i++) {
				c = alphabet.charAt(i);
				lookupTable[c - BASE_CHAR] = (byte) i;
				// 支持小写字母解码
				if (c >= 'A' && c <= 'Z') {
					lookupTable[Character.toLowerCase(c) - BASE_CHAR] = (byte) i;
				}
			}
		}

		@Override
		public byte[] decode(final CharSequence encoded) {
			int i, index, lookup, offset, digit;
			final String base32 = encoded.toString();
			final int len = base32.endsWith("=") ? base32.indexOf("=") * 5 / 8 : base32.length() * 5 / 8;
			final byte[] bytes = new byte[len];

			for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
				lookup = base32.charAt(i) - BASE_CHAR;

				/* Skip chars outside the lookup table */
				if (lookup < 0 || lookup >= lookupTable.length) {
					continue;
				}

				digit = lookupTable[lookup];

				/* If this digit is not in the table, ignore it */
				if (digit < 0) {
					continue;
				}

				if (index <= 3) {
					index = (index + 5) % 8;
					if (index == 0) {
						bytes[offset] |= digit;
						offset++;
						if (offset >= bytes.length) {
							break;
						}
					} else {
						bytes[offset] |= digit << (8 - index);
					}
				} else {
					index = (index + 5) % 8;
					bytes[offset] |= (digit >>> index);
					offset++;

					if (offset >= bytes.length) {
						break;
					}
					bytes[offset] |= digit << (8 - index);
				}
			}
			return bytes;
		}
	}
}
