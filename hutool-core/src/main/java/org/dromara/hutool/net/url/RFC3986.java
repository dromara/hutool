/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.net.url;

import org.dromara.hutool.codec.PercentCodec;

/**
 * <a href="https://www.ietf.org/rfc/rfc3986.html">RFC3986</a> 编码实现<br>
 * 定义见：<a href="https://www.ietf.org/rfc/rfc3986.html#appendix-A">https://www.ietf.org/rfc/rfc3986.html#appendix-A</a>
 *
 * @author looly
 * @since 5.7.16
 */
public class RFC3986 {

	/**
	 * gen-delims = ":" / "/" / "?" / "#" / "[" / "]" / "@"
	 */
	public static final PercentCodec GEN_DELIMS = PercentCodec.Builder.of(":/?#[]@").build();

	/**
	 * sub-delims = "!" / "$" / "{@code &}" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 */
	public static final PercentCodec SUB_DELIMS = PercentCodec.Builder.of("!$&'()*+,;=").build();

	/**
	 * reserved = gen-delims / sub-delims<br>
	 * see：<a href="https://www.ietf.org/rfc/rfc3986.html#section-2.2">https://www.ietf.org/rfc/rfc3986.html#section-2.2</a>
	 */
	public static final PercentCodec RESERVED = PercentCodec.Builder.of(GEN_DELIMS).or(SUB_DELIMS).build();

	/**
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"<br>
	 * see: <a href="https://www.ietf.org/rfc/rfc3986.html#section-2.3">https://www.ietf.org/rfc/rfc3986.html#section-2.3</a>
	 */
	public static final PercentCodec UNRESERVED = PercentCodec.Builder.of(unreservedChars()).build();

	/**
	 * pchar = unreserved / pct-encoded / sub-delims / ":" / "@"
	 */
	public static final PercentCodec PCHAR = PercentCodec.Builder.of(UNRESERVED).or(SUB_DELIMS).addSafes(":@").build();

	/**
	 * segment  = pchar<br>
	 * see: <a href="https://www.ietf.org/rfc/rfc3986.html#section-3.3">https://www.ietf.org/rfc/rfc3986.html#section-3.3</a>
	 */
	public static final PercentCodec SEGMENT = PCHAR;
	/**
	 * segment-nz-nc  = SEGMENT ; non-zero-length segment without any colon ":"
	 */
	public static final PercentCodec SEGMENT_NZ_NC = PercentCodec.Builder.of(SEGMENT).removeSafe(':').build();

	/**
	 * path = segment / "/"
	 */
	public static final PercentCodec PATH = PercentCodec.Builder.of(SEGMENT).addSafe('/').build();

	/**
	 * query = pchar / "/" / "?"
	 */
	public static final PercentCodec QUERY = PercentCodec.Builder.of(PCHAR).addSafes("/?").build();

	/**
	 * fragment     = pchar / "/" / "?"
	 */
	public static final PercentCodec FRAGMENT = QUERY;

	/**
	 * query中的value<br>
	 * value不能包含"{@code &}"，可以包含 "="
	 */
	public static final PercentCodec QUERY_PARAM_VALUE = PercentCodec.Builder.of(QUERY).removeSafe('&').build();

	/**
	 * query中的key<br>
	 * key不能包含"{@code &}" 和 "="
	 */
	public static final PercentCodec QUERY_PARAM_NAME = PercentCodec.Builder.of(QUERY_PARAM_VALUE).removeSafe('=').build();

	/**
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 *
	 * @return unreserved字符
	 */
	private static StringBuilder unreservedChars() {
		final StringBuilder sb = new StringBuilder();

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
