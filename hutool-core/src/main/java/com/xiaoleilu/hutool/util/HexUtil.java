package com.xiaoleilu.hutool.util;

import java.nio.charset.Charset;

/**
 * 十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。<br>
 * 例如十进制数57，在二进制写作111001，在16进制写作39。<br>
 * 像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20<br>
 * 
 * 参考：https://my.oschina.net/xinxingegeya/blog/287476
 * 
 * @author Looly
 *
 */
public final class HexUtil {
	
	private HexUtil() {}

	/**
	 * 用于建立十六进制字符的输出的小写字符数组
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 用于建立十六进制字符的输出的大写字符数组
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * 判断给定字符串是否为16进制数<br>
	 * 如果是，需要使用对应数字类型对象的<code>decode</code>方法解码<br>
	 * 例如：{@code Integer.decode}方法解码int类型的16进制数字
	 * @param value 值
	 * @return 是否为16进制
	 */
	public static boolean isHexNumber(String value) {
		int index = (value.startsWith("-") ? 1 : 0);
		return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
	}

	//---------------------------------------------------------------------------------------------------- encode
	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data byte[]
	 * @return 十六进制char[]
	 */
	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}
	
	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param str 字符串
	 * @param charset 编码
	 * @return 十六进制char[]
	 */
	public static char[] encodeHex(String str, Charset charset) {
		return encodeHex(StrUtil.bytes(str, charset), true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data byte[]
	 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制char[]
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data byte[]
	 * @return 十六进制String
	 */
	public static String encodeHexStr(byte[] data) {
		return encodeHexStr(data, true);
	}
	
	/**
	 * 将字节数组转换为十六进制字符串，结果为小写
	 *
	 * @param data 被编码的字符串
	 * @param charset 编码
	 * @return 十六进制String
	 */
	public static String encodeHexStr(String data, Charset charset) {
		return encodeHexStr(StrUtil.bytes(data, charset), true);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data byte[]
	 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制String
	 */
	public static String encodeHexStr(byte[] data, boolean toLowerCase) {
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}
	
	//---------------------------------------------------------------------------------------------------- decode
	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexStr 十六进制String
	 * @return 字符串
	 */
	public static String decodeHexStr(String hexStr) {
		return decodeHexStr(hexStr, CharsetUtil.CHARSET_UTF_8);
	}
	
	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexStr 十六进制String
	 * @param charset 编码
	 * @return 字符串
	 */
	public static String decodeHexStr(String hexStr, Charset charset) {
		if(StrUtil.isEmpty(hexStr)){
			return hexStr;
		}
		return decodeHexStr(hexStr.toCharArray(), charset);
	}
	
	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexData 十六进制char[]
	 * @param charset 编码
	 * @return 字符串
	 */
	public static String decodeHexStr(char[] hexData, Charset charset) {
		return StrUtil.str(decodeHex(hexData), charset);
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 *
	 * @param hexData 十六进制char[]
	 * @return byte[]
	 * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
	 */
	public static byte[] decodeHex(char[] hexData) {

		int len = hexData.length;

		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(hexData[j], j) << 4;
			j++;
			f = f | toDigit(hexData[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}
	
	/**
	 * 将十六进制字符数组转换为字符串
	 *
	 * @param hexStr 十六进制String
	 * @return byte[]
	 */
	public static byte[] decodeHex(String hexStr) {
		if(StrUtil.isEmpty(hexStr)){
			return null;
		}
		return decodeHex(hexStr.toCharArray());
	}
	
	//---------------------------------------------------------------------------------------- Private method start
	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data byte[]
	 * @param toDigits 用于控制输出的char[]
	 * @return 十六进制String
	 */
	private static String encodeHexStr(byte[] data, char[] toDigits) {
		return new String(encodeHex(data, toDigits));
	}
	
	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data byte[]
	 * @param toDigits 用于控制输出的char[]
	 * @return 十六进制char[]
	 */
	private static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * 将十六进制字符转换成一个整数
	 *
	 * @param ch 十六进制char
	 * @param index 十六进制字符在字符数组中的位置
	 * @return 一个整数
	 * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
	 */
	private static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}
	//---------------------------------------------------------------------------------------- Private method end
}