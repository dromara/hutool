package com.xiaoleilu.hutool.lang;

import java.nio.charset.Charset;
import java.util.Arrays;

import com.xiaoleilu.hutool.util.StrUtil;

public class Base64 {
	/** base64码表 */
	private static char[] base64EncodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static final int[] INV = new int[256];

	static {
		Arrays.fill(INV, -1);
		for (int i = 0, iS = base64EncodeTable.length; i < iS; i++) {
			INV[base64EncodeTable[i]] = i;
		}
		INV['='] = 0;
	}
	
	//-------------------------------------------------------------------- encode
	/**
	 * 编码为Base64
	 * 
	 * @param arr 被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 */
	public static byte[] encode(byte[] arr, boolean lineSep) {
		int len = arr != null ? arr.length : 0;
		if (len == 0) {
			return new byte[0];
		}

		int evenlen = (len / 3) * 3;
		int cnt = ((len - 1) / 3 + 1) << 2;
		int destlen = cnt + (lineSep ? (cnt - 1) / 76 << 1 : 0);
		byte[] dest = new byte[destlen];

		for (int s = 0, d = 0, cc = 0; s < evenlen;) {
			int i = (arr[s++] & 0xff) << 16 | (arr[s++] & 0xff) << 8 | (arr[s++] & 0xff);

			dest[d++] = (byte) base64EncodeTable[(i >>> 18) & 0x3f];
			dest[d++] = (byte) base64EncodeTable[(i >>> 12) & 0x3f];
			dest[d++] = (byte) base64EncodeTable[(i >>> 6) & 0x3f];
			dest[d++] = (byte) base64EncodeTable[i & 0x3f];

			if (lineSep && ++cc == 19 && d < destlen - 2) {
				dest[d++] = '\r';
				dest[d++] = '\n';
				cc = 0;
			}
		}

		int left = len - evenlen;
		if (left > 0) {
			int i = ((arr[evenlen] & 0xff) << 10) | (left == 2 ? ((arr[len - 1] & 0xff) << 2) : 0);

			dest[destlen - 4] = (byte) base64EncodeTable[i >> 12];
			dest[destlen - 3] = (byte) base64EncodeTable[(i >>> 6) & 0x3f];
			dest[destlen - 2] = left == 2 ? (byte) base64EncodeTable[i & 0x3f] : (byte) '=';
			dest[destlen - 1] = '=';
		}
		return dest;
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(String source) {
		return encode(source, Charset.defaultCharset());
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, String charset) {
		return encode(StrUtil.bytes(source, charset), charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, Charset charset) {
		return encode(StrUtil.bytes(source, charset), charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source) {
		return encode(source, Charset.defaultCharset());
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source, String charset) {
		return StrUtil.str(encode(source, false), charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source, Charset charset) {
		return StrUtil.str(encode(source, false), charset);
	}

	//-------------------------------------------------------------------- decode
	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source) {
		return decodeStr(source, Charset.defaultCharset());
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, String charset) {
		return StrUtil.str(decode(source, charset), charset);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, Charset charset) {
		return StrUtil.str(decode(source, charset), charset);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source) {
		return decode(source, Charset.defaultCharset());
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source, String charset) {
		return decode(StrUtil.bytes(source, charset));
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source, Charset charset) {
		return decode(StrUtil.bytes(source, charset));
	}

	/**
	 * 解码Base64
	 * 
	 * @param arr byte数组
	 * @return 解码后的byte数组
	 */
	public static byte[] decode(byte[] arr) {
		int length = arr.length;
		if (length == 0) {
			return new byte[0];
		}

		int sndx = 0, endx = length - 1;
		int pad = arr[endx] == '=' ? (arr[endx - 1] == '=' ? 2 : 1) : 0;
		int cnt = endx - sndx + 1;
		int sepCnt = length > 76 ? (arr[76] == '\r' ? cnt / 78 : 0) << 1 : 0;
		int len = ((cnt - sepCnt) * 6 >> 3) - pad;
		byte[] dest = new byte[len];

		int d = 0;
		for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
			int i = INV[arr[sndx++]] << 18 | INV[arr[sndx++]] << 12 | INV[arr[sndx++]] << 6 | INV[arr[sndx++]];

			dest[d++] = (byte) (i >> 16);
			dest[d++] = (byte) (i >> 8);
			dest[d++] = (byte) i;

			if (sepCnt > 0 && ++cc == 19) {
				sndx += 2;
				cc = 0;
			}
		}

		if (d < len) {
			int i = 0;
			for (int j = 0; sndx <= endx - pad; j++) {
				i |= INV[arr[sndx++]] << (18 - j * 6);
			}
			for (int r = 16; d < len; r -= 8) {
				dest[d++] = (byte) (i >> r);
			}
		}

		return dest;
	}
}
