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

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.net.url.UrlUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;

/**
 * 文件资源访问对象，支持{@link Path} 和 {@link File} 访问
 *
 * @author looly
 */
public class FileResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final File file;
	private final long lastModified;
	private final String name;

	// ----------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 *
	 * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
	 */
	public FileResource(final String path) {
		this(FileUtil.file(path));
	}

	/**
	 * 构造，文件名使用文件本身的名字，带扩展名
	 *
	 * @param path 文件
	 * @since 4.4.1
	 */
	public FileResource(final Path path) {
		this(path.toFile());
	}

	/**
	 * 构造，文件名使用文件本身的名字，带扩展名
	 *
	 * @param file 文件
	 */
	public FileResource(final File file) {
		this(file, null);
	}

	/**
	 * 构造
	 *
	 * @param file 文件
	 * @param fileName 文件名，带扩展名，如果为null获取文件本身的文件名
	 */
	public FileResource(final File file, final String fileName) {
		Assert.notNull(file, "File must be not null !");
		this.file = file;
		this.lastModified = file.lastModified();
		this.name = ObjUtil.defaultIfNull(fileName, file::getName);
	}

	// ----------------------------------------------------------------------- Constructor end

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl(){
		return UrlUtil.getURL(this.file);
	}

	@Override
	public long size() {
		return this.file.length();
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		return FileUtil.getInputStream(this.file);
	}

	/**
	 * 获取文件
	 *
	 * @return 文件
	 */
	public File getFile() {
		return this.file;
	}

	@Override
	public boolean isModified() {
		return this.lastModified != file.lastModified();
	}

	/**
	 * 返回路径
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return this.file.toString();
	}
}
