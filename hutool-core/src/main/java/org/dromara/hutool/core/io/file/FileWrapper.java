/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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

	protected File file;
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
