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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.text.StrUtil;

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

	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
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
	public static String decodeForPath(final String str, final Charset charset) {
		return decode(str, charset, false);
	}

	/**
	 * 解码<br>
	 * 规则见：<a href="https://url.spec.whatwg.org/#urlencoded-parsing">https://url.spec.whatwg.org/#urlencoded-parsing</a>
	 * <pre>
	 *   1. 将+和%20转换为空格(" ");
	 *   2. 将"%xy"转换为文本形式,xy是两位16进制的数值;
	 *   3. 跳过不符合规范的%形式，直接输出
	 * </pre>
	 *
	 * @param str 包含URL编码后的字符串
	 * @return 解码后的字符串
	 */
	public static String decode(final String str) {
		return decode(str, DEFAULT_CHARSET);
	}

	/**
	 * 解码<br>
	 * 规则见：<a href="https://url.spec.whatwg.org/#urlencoded-parsing">https://url.spec.whatwg.org/#urlencoded-parsing</a>
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
	public static String decode(final String str, final Charset charset) {
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
	 * @return 解码后的字符串
	 */
	public static String decode(final String str, final boolean isPlusToSpace) {
		return decode(str, DEFAULT_CHARSET, isPlusToSpace);
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
	public static String decode(final String str, final Charset charset, final boolean isPlusToSpace) {
		if (null == charset) {
			return str;
		}
		return StrUtil.str(decode(ByteUtil.toBytes(str, charset), isPlusToSpace), charset);
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
	public static byte[] decode(final byte[] bytes) {
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
	public static byte[] decode(final byte[] bytes, final boolean isPlusToSpace) {
		if (bytes == null) {
			return null;
		}
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length * 3);
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
}
