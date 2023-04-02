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

package org.dromara.hutool.io.stream;

import org.dromara.hutool.util.ByteUtil;
import org.dromara.hutool.util.CharsetUtil;

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
		super(ByteUtil.toBytes(str, charset));
	}
}
