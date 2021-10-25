package cn.hutool.core.net;

import cn.hutool.core.codec.PercentCodec;

/**
 * rfc3986 : https://www.ietf.org/rfc/rfc3986.html 编码实现
 *
 * @author looly
 * @since 5.7.16
 */
public class RFC3986 {

	/**
	 * gen-delims = ":" / "/" / "?" / "#" / "[" / "]" / "@"
	 */
	public static final PercentCodec GEN_DELIMS = PercentCodec.of(":/?#[]&");

	/**
	 * sub-delims = "!" / "$" / "{@code &}" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 */
	public static final PercentCodec SUB_DELIMS = PercentCodec.of("!$&'()*+,;=");

	/**
	 * reserved = gen-delims / sub-delims
	 */
	public static final PercentCodec RESERVED = GEN_DELIMS.orNew(SUB_DELIMS);

	/**
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 */
	public static final PercentCodec UNRESERVED = PercentCodec.of(unreservedChars());

	/**
	 * pchar = unreserved / pct-encoded / sub-delims / ":" / "@"
	 */
	public static final PercentCodec PCHAR = UNRESERVED.orNew(SUB_DELIMS).or(PercentCodec.of(":@"));

	/**
	 * segment  = pchar
	 */
	public static final PercentCodec SEGMENT = PCHAR;
	/**
	 * segment-nz-nc  = SEGMENT ; non-zero-length segment without any colon ":"
	 */
	public static final PercentCodec SEGMENT_NZ_NC = PercentCodec.of(SEGMENT).removeSafe(':');

	/**
	 * path = segment / "/"
	 */
	public static final PercentCodec PATH = SEGMENT.orNew(PercentCodec.of("/"));

	/**
	 * query = pchar / "/" / "?"
	 */
	public static final PercentCodec QUERY = PCHAR.orNew(PercentCodec.of("/?"));

	/**
	 * fragment     = pchar / "/" / "?"
	 */
	public static final PercentCodec FRAGMENT = QUERY;

	/**
	 * query中的key
	 */
	public static final PercentCodec QUERY_PARAM_NAME = PercentCodec.of(QUERY).removeSafe('&').removeSafe('=');

	/**
	 * query中的value
	 */
	public static final PercentCodec QUERY_PARAM_VALUE = PercentCodec.of(QUERY).removeSafe('&');

	/**
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 *
	 * @return unreserved字符
	 */
	private static StringBuilder unreservedChars() {
		StringBuilder sb = new StringBuilder();

		// ALPHA
		for (char c = 'A'; c <= 'Z'; c++) {
			sb.append(c);
		}
		for (char c = 'a'; c <= 'z'; c++) {
			sb.append(c);
		}

		// DIGIT
		for (char c = '0'; c <= '9'; c++) {
			sb.append(c);
		}

		// "-" / "." / "_" / "~"
		sb.append("_.-~");

		return sb;
	}
}
