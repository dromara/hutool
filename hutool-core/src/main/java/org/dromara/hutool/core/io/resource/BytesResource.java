/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 基于byte[]的资源获取器<br>
 * 注意：此对象中getUrl方法始终返回null
 *
 * @author looly
 * @since 4.0.9
 */
public class BytesResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final byte[] bytes;
	private final String name;

	/**
	 * 构造
	 *
	 * @param bytes 字节数组
	 */
	public BytesResource(final byte[] bytes) {
		this(bytes, null);
	}

	/**
	 * 构造
	 *
	 * @param bytes 字节数组
	 * @param name 资源名称
	 */
	public BytesResource(final byte[] bytes, final String name) {
		this.bytes = bytes;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public long size() {
		return this.bytes.length;
	}

	@Override
	public InputStream getStream() {
		return new ByteArrayInputStream(this.bytes);
	}

	@Override
	public String readStr(final Charset charset) throws IORuntimeException {
		return StrUtil.str(this.bytes, charset);
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return this.bytes;
	}

}
