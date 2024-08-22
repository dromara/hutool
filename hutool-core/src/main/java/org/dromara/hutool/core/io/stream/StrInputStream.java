/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.io.stream;

import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

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
