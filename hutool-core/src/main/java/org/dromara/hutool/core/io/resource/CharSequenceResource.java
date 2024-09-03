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

package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * {@link CharSequence}资源，字符串做为资源
 *
 * @author looly
 * @since 5.5.2
 */
public class CharSequenceResource extends BytesResource {
	private static final long serialVersionUID = 1L;

	/**
	 * 由于{@link Charset} 无法序列化，此处使用编码名称
	 */
	private final String charsetName;

	/**
	 * 构造，使用UTF8编码
	 *
	 * @param data 资源数据
	 */
	public CharSequenceResource(final CharSequence data) {
		this(data, null);
	}

	/**
	 * 构造，使用UTF8编码
	 *
	 * @param data 资源数据
	 * @param name 资源名称
	 */
	public CharSequenceResource(final CharSequence data, final String name) {
		this(data, name, CharsetUtil.UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param data    资源数据
	 * @param name    资源名称
	 * @param charset 编码
	 */
	public CharSequenceResource(final CharSequence data, final String name, final Charset charset) {
		super(ByteUtil.toBytes(data, charset), name);
		this.charsetName = charset.name();
	}

	/**
	 * 读取为字符串
	 *
	 * @return 字符串
	 */
	public String readStr() {
		return readStr(getCharset());
	}

	/**
	 * 获取编码名
	 *
	 * @return 编码名
	 */
	public String getCharsetName() {
		return this.charsetName;
	}

	/**
	 * 获取编码
	 *
	 * @return 编码
	 */
	public Charset getCharset() {
		return CharsetUtil.charset(this.charsetName);
	}
}
