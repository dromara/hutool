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
	 * query中的value，默认除"-", "_", ".", "*"外都编码<br>
	 * 这个类似于JDK提供的{@link java.net.URLEncoder}
	 */
	public static final PercentCodec ALL = PercentCodec.of(RFC3986.UNRESERVED)
			.removeSafe('~').addSafe('*').setEncodeSpaceAsPlus(true);
}
