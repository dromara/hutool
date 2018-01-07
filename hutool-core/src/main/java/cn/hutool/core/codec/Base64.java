package cn.hutool.core.codec;

import java.nio.charset.Charset;

/**
 * Base64工具类，提供Base64的编码和解码方案<br>
 * base64编码是用64（2的6次方）个ASCII字符来表示256（2的8次方）个ASCII字符，<br>
 * 也就是三位二进制数组经过编码后变为四位的ASCII字符显示，长度比原来增加1/3。
 * 
 * @author Looly
 *
 */
public class Base64 {

	// -------------------------------------------------------------------- encode
	/**
	 * 编码为Base64，非URL安全的
	 * 
	 * @param arr 被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 */
	public static byte[] encode(byte[] arr, boolean lineSep) {
		return Base64Encoder.encode(arr, lineSep);
	}

	/**
	 * 编码为Base64，URL安全的
	 * 
	 * @param arr 被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 * @since 3.0.6
	 */
	public static byte[] encodeUrlSafe(byte[] arr, boolean lineSep) {
		return Base64Encoder.encodeUrlSafe(arr, lineSep);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(String source) {
		return Base64Encoder.encode(source);
	}

	/**
	 * base64编码，URL安全
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(String source) {
		return Base64Encoder.encodeUrlSafe(source);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, String charset) {
		return Base64Encoder.encode(source, charset);
	}

	/**
	 * base64编码,URL安全
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(String source, String charset) {
		return Base64Encoder.encodeUrlSafe(source, charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(String source, Charset charset) {
		return Base64Encoder.encode(source, charset);
	}

	/**
	 * base64编码，URL安全的
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(String source, Charset charset) {
		return Base64Encoder.encodeUrlSafe(source, charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source) {
		return Base64Encoder.encode(source);
	}

	/**
	 * base64编码,URL安全的
	 * 
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(byte[] source) {
		return Base64Encoder.encodeUrlSafe(source);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source, String charset) {
		return Base64Encoder.encode(source, charset);
	}

	/**
	 * base64编码，URL安全的
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(byte[] source, String charset) {
		return Base64Encoder.encodeUrlSafe(source, charset);
	}

	/**
	 * base64编码
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source, Charset charset) {
		return Base64Encoder.encode(source, charset);
	}

	/**
	 * base64编码，URL安全的
	 * 
	 * @param source 被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @since 3.0.6
	 */
	public static String encodeUrlSafe(byte[] source, Charset charset) {
		return Base64Encoder.encodeUrlSafe(source, charset);
	}

	/**
	 * 编码为Base64<br>
	 * 如果isMultiLine为<code>true</code>，则每76个字符一个换行符，否则在一行显示
	 * 
	 * @param arr 被编码的数组
	 * @param isMultiLine 在76个char之后是CRLF还是EOF
	 * @param isUrlSafe 是否使用URL安全字符，一般为<code>false</code>
	 * @return 编码后的bytes
	 */
	public static byte[] encode(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
		return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
	}

	// -------------------------------------------------------------------- decode
	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source) {
		return Base64Decoder.decodeStr(source);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, String charset) {
		return Base64Decoder.decodeStr(source, charset);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(String source, Charset charset) {
		return Base64Decoder.decodeStr(source, charset);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source) {
		return Base64Decoder.decode(source);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source, String charset) {
		return Base64Decoder.decode(source, charset);
	}

	/**
	 * base64解码
	 * 
	 * @param source 被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(String source, Charset charset) {
		return Base64Decoder.decode(source, charset);
	}

	/**
	 * 解码Base64
	 * 
	 * @param in 输入
	 * @return 解码后的bytes
	 */
	public static byte[] decode(byte[] in) {
		return Base64Decoder.decode(in);
	}
}
