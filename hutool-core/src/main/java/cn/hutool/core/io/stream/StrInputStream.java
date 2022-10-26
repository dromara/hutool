package cn.hutool.core.io.stream;

import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

/**
 * 基于字符串的InputStream
 *
 * @author looly
 * @since 6.0.0
 */
public class StrInputStream extends ByteArrayInputStream {

	/**
	 * 创建StrInputStream
	 *
	 * @param str 字符串
	 * @return StrInputStream
	 */
	public static StrInputStream ofUtf8(final CharSequence str) {
		return of(str, CharsetUtil.UTF_8);
	}

	/**
	 * 创建StrInputStream
	 *
	 * @param str     字符串
	 * @param charset 编码
	 * @return StrInputStream
	 */
	public static StrInputStream of(final CharSequence str, final Charset charset) {
		return new StrInputStream(str, charset);
	}

	/**
	 * 构造
	 *
	 * @param str     字符串
	 * @param charset 编码
	 */
	public StrInputStream(final CharSequence str, final Charset charset) {
		super(StrUtil.bytes(str, charset));
	}
}
