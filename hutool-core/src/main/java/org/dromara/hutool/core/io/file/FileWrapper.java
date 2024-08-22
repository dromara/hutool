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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 文件包装器，扩展文件对象
 *
 * @author Looly
 */
public class FileWrapper implements Wrapper<File>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 被包装的文件
	 */
	protected File file;
	/**
	 * 编码
	 */
	protected Charset charset;

	/**
	 * 默认编码：UTF-8
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	// ------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param file    文件（非{@code null}）
	 * @param charset 编码，使用 {@link CharsetUtil}，传入{@code null}则使用默认编码{@link #DEFAULT_CHARSET}
	 */
	public FileWrapper(final File file, final Charset charset) {
		this.file = Assert.notNull(file);
		this.charset = ObjUtil.defaultIfNull(charset, DEFAULT_CHARSET);
	}
	// ------------------------------------------------------- Constructor end

	// ------------------------------------------------------- Setters and Getters start start

	/**
	 * 获得文件
	 *
	 * @return 文件
	 */
	@Override
	public File getRaw() {
		return file;
	}

	/**
	 * 设置文件
	 *
	 * @param file 文件
	 * @return 自身
	 */
	public FileWrapper setFile(final File file) {
		this.file = file;
		return this;
	}

	/**
	 * 获得字符集编码
	 *
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符集编码
	 *
	 * @param charset 编码
	 * @return 自身
	 */
	public FileWrapper setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}
	// ------------------------------------------------------- Setters and Getters start end

	/**
	 * 可读的文件大小
	 *
	 * @return 大小
	 */
	public String readableFileSize() {
		return FileUtil.readableFileSize(file.length());
	}
}
