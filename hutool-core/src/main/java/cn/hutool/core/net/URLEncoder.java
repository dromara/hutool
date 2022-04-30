package cn.hutool.core.net;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * URL编码器，提供百分号编码实现
 *
 * @author looly
 * @since 6.0.0
 */
public class URLEncoder {

	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。。不参与编码的字符：<br>
	 * <pre>
	 *     unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * </pre>
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(final String url) {
		return encodeAll(url, DEFAULT_CHARSET);
	}

	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。不参与编码的字符：<br>
	 * <pre>
	 *     unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * </pre>
	 *
	 * @param url     URL
	 * @param charset 编码，为null表示不编码
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(final String url, final Charset charset) throws UtilException {
		return RFC3986.UNRESERVED.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url URL
	 * @return 编码后的URL
	 */
	public static String encodeQuery(final String url) {
		return encodeQuery(url, DEFAULT_CHARSET);
	}

	/**
	 * 编码字符为URL中查询语句<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 */
	public static String encodeQuery(final String url, final Charset charset) {
		return RFC3986.QUERY.encode(url, charset);
	}

	/**
	 * 单独编码URL中的空白符，空白符编码为%20
	 *
	 * @param urlStr URL字符串
	 * @return 编码后的字符串
	 * @since 4.5.14
	 */
	public static String encodeBlank(final CharSequence urlStr) {
		if (urlStr == null) {
			return null;
		}

		final int len = urlStr.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = urlStr.charAt(i);
			if (CharUtil.isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
