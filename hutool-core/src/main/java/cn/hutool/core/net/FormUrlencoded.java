package cn.hutool.core.net;

import cn.hutool.core.codec.PercentCodec;

/**
 * application/x-www-form-urlencoded，遵循W3C HTML Form content types规范，如空格须转+，+须被编码<br>
 * 规范见：https://url.spec.whatwg.org/#urlencoded-serializing
 *
 * @since 5.7.16
 */
public class FormUrlencoded {

	/**
	 * query中的value<br>
	 * value不能包含"{@code &}"，可以包含 "="
	 */
	public static final PercentCodec QUERY_PARAM_VALUE = PercentCodec.of(RFC3986.QUERY_PARAM_VALUE)
			.setEncodeSpaceAsPlus(true).removeSafe('+');

	/**
	 * query中的key<br>
	 * key不能包含"{@code &}" 和 "="
	 */
	public static final PercentCodec QUERY_PARAM_NAME = QUERY_PARAM_VALUE.removeSafe('=');
}
