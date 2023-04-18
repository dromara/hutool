package cn.hutool.core.net;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * URL解码，数据内容的类型是 application/x-www-form-urlencoded。
 *
 * <pre>
 * 1. 将%20转换为空格 ;
 * 2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
 * 3. 跳过不符合规范的%形式，直接输出
 * </pre>
 *
 * @author looly
 */
public class URLDecoder implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final byte ESCAPE_CHAR = '%';

	/**
	 * 解码，不对+解码
	 *
	 * <ol>
	 *     <li>将%20转换为空格</li>
	 *     <li>将 "%xy"转换为文本形式,xy是两位16进制的数值</li>
	 *     <li>跳过不符合规范的%形式，直接输出</li>
	 * </ol>
	 *
	 * @param str     包含URL编码后的字符串
	 * @param charset 编码
	 * @return 解码后的字符串
	 */
	public static String decodeForPath(String str, Charset charset) {
		return decode(str, charset, false);
	}

	/**
	 * 解码<br>
	 * 规则见：https://url.spec.whatwg.org/#urlencoded-parsing
	 * <pre>
	 *   1. 将+和%20转换为空格(" ");
	 *   2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
	 *   3. 跳过不符合规范的%形式，直接输出
	 * </pre>
	 *
	 * @param str     包含URL编码后的字符串
	 * @param charset 编码
	 * @return 解码后的字符串
	 */
	public static String decode(String str, Charset charset) {
		return decode(str, charset, true);
	}

	/**
	 * 解码
	 * <pre>
	 *   1. 将%20转换为空格 ;
	 *   2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
	 *   3. 跳过不符合规范的%形式，直接输出
	 * </pre>
	 *
	 * @param str           包含URL编码后的字符串
	 * @param isPlusToSpace 是否+转换为空格
	 * @param charset       编码，{@code null}表示不做编码
	 * @return 解码后的字符串
	 */
	public static String decode(String str, Charset charset, boolean isPlusToSpace) {
		if(null == str || null == charset){
			return str;
		}

		final int length = str.length();
		if(0 == length){
			return StrUtil.EMPTY;
		}

		final StringBuilder result = new StringBuilder(length / 3);

		int begin = 0;
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if(ESCAPE_CHAR == c || CharUtil.isHexChar(c)){
				continue;
			}

			// 遇到非需要处理的字符跳过
			// 处理之前的hex字符
			if(i > begin){
				result.append(decodeSub(str, begin, i, charset, isPlusToSpace));
			}

			// 非Hex字符，忽略本字符
			if('+' == c && isPlusToSpace){
				c = CharUtil.SPACE;
			}

			// 非Hex字符，忽略本字符
			result.append(c);
			begin = i + 1;
		}

		// 处理剩余字符
		if(begin < length){
			result.append(decodeSub(str, begin, length, charset, isPlusToSpace));
		}

		return result.toString();
	}

	/**
	 * 解码
	 * <pre>
	 *   1. 将+和%20转换为空格 ;
	 *   2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
	 *   3. 跳过不符合规范的%形式，直接输出
	 * </pre>
	 *
	 * @param bytes url编码的bytes
	 * @return 解码后的bytes
	 */
	public static byte[] decode(byte[] bytes) {
		return decode(bytes, true);
	}

	/**
	 * 解码
	 * <pre>
	 *   1. 将%20转换为空格 ;
	 *   2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
	 *   3. 跳过不符合规范的%形式，直接输出
	 * </pre>
	 *
	 * @param bytes         url编码的bytes
	 * @param isPlusToSpace 是否+转换为空格
	 * @return 解码后的bytes
	 * @since 5.6.3
	 */
	public static byte[] decode(byte[] bytes, boolean isPlusToSpace) {
		if (bytes == null) {
			return null;
		}
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);
		int b;
		for (int i = 0; i < bytes.length; i++) {
			b = bytes[i];
			if (b == '+') {
				buffer.write(isPlusToSpace ? CharUtil.SPACE : b);
			} else if (b == ESCAPE_CHAR) {
				if (i + 1 < bytes.length) {
					final int u = CharUtil.digit16(bytes[i + 1]);
					if (u >= 0 && i + 2 < bytes.length) {
						final int l = CharUtil.digit16(bytes[i + 2]);
						if (l >= 0) {
							buffer.write((char) ((u << 4) + l));
							i += 2;
							continue;
						}
					}
				}
				// 跳过不符合规范的%形式
				buffer.write(b);
			} else {
				buffer.write(b);
			}
		}
		return buffer.toByteArray();
	}

	/**
	 * 解码子串
	 *
	 * @param str 字符串
	 * @param begin 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @param charset 编码
	 * @param isPlusToSpace 是否+转换为空格
	 * @return 解码后的字符串
	 */
	private static String decodeSub(final String str, final int begin, final int end,
									final Charset charset, final boolean isPlusToSpace){
		return new String(decode(
			// 截取需要decode的部分
			str.substring(begin, end).getBytes(CharsetUtil.CHARSET_ISO_8859_1), isPlusToSpace
		), charset);
	}
}
