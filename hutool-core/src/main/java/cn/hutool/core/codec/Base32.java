package cn.hutool.core.codec;

import java.nio.charset.Charset;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Base32 - encodes and decodes RFC3548 Base32 (see http://www.faqs.org/rfcs/rfc3548.html )<br>
 * base32就是用32（2的5次方）个特定ASCII码来表示256个ASCII码。<br>
 * 所以，5个ASCII字符经过base32编码后会变为8个字符（公约数为40），长度增加3/5.不足8n用“=”补足。
 * see http://blog.csdn.net/earbao/article/details/44453937
 * @author Looly
 *
 */
public class Base32 {
	
	private Base32() {}

	private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
	private static final int[] BASE32_LOOKUP = {//
			0xFF, 0xFF, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, // '0', '1', '2', '3', '4', '5', '6', '7'
			0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // '8', '9', ':', ';', '<', '=', '>', '?'
			0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, // '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G'
			0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, // 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
			0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, // 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W'
			0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // 'X', 'Y', 'Z', '[', '\', ']', '^', '_'
			0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, // '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g'
			0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, // 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'
			0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, // 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'
			0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF // 'x', 'y', 'z', '{', '|', '}', '~', 'DEL'
	};

	//----------------------------------------------------------------------------------------- encode
	/**
	 * 编码
	 * @param bytes 数据
	 * @return base32
	 */
	public static String encode(final byte[] bytes) {
		int i = 0;
		int index = 0;
		int digit;
		int currByte;
		int nextByte;
		StringBuilder base32 = new StringBuilder((bytes.length + 7) * 8 / 5);

		while (i < bytes.length) {
			currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256); // unsign

			/* Is the current digit going to span a byte boundary? */
			if (index > 3) {
				if ((i + 1) < bytes.length) {
					nextByte = (bytes[i + 1] >= 0) ? bytes[i + 1] : (bytes[i + 1] + 256);
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
			base32.append(BASE32_CHARS.charAt(digit));
		}

		return base32.toString();
	}
	
	/**
	 * base32编码
	 * 
	 * @param source 被编码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(String source) {
		return encode(source, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * base32编码
	 * 
	 * @param source 被编码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, String charset) {
		return encode(StrUtil.bytes(source, charset));
	}

	/**
	 * base32编码
	 * 
	 * @param source 被编码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, Charset charset) {
		return encode(StrUtil.bytes(source, charset));
	}

	//----------------------------------------------------------------------------------------- decode
	/**
	 * 解码
	 * @param base32 base32编码
	 * @return 数据
	 */
	public static byte[] decode(final String base32) {
		int i, index, lookup, offset, digit;
		byte[] bytes = new byte[base32.length() * 5 / 8];

		for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
			lookup = base32.charAt(i) - '0';

			/* Skip chars outside the lookup table */
			if (lookup < 0 || lookup >= BASE32_LOOKUP.length) {
				continue;
			}

			digit = BASE32_LOOKUP[lookup];

			/* If this digit is not in the table, ignore it */
			if (digit == 0xFF) {
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
	
	/**
	 * base32解码
	 * 
	 * @param source 被解码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source) {
		return decodeStr(source, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * base32解码
	 * 
	 * @param source 被解码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, String charset) {
		return StrUtil.str(decode(source), charset);
	}
	
	/**
	 * base32解码
	 * 
	 * @param source 被解码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, Charset charset) {
		return StrUtil.str(decode(source), charset);
	}
}
