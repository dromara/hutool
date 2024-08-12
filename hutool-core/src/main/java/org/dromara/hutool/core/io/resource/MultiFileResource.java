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

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

/**
 * 多文件组合资源<br>
 * 此资源为一个利用游标自循环资源，只有调用{@link #next()} 方法才会获取下一个资源，使用完毕后调用{@link #reset()}方法重置游标
 *
 * @author looly
 */
public class MultiFileResource extends MultiResource {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param files 文件资源列表
	 */
	public MultiFileResource(final Collection<File> files) {
		add(files);
	}

	/**
	 * 构造
	 *
	 * @param files 文件资源列表
	 */
	public MultiFileResource(final File... files) {
		add(files);
	}

	/**
	 * 构造
	 *
	 * @param files 文件资源列表
	 */
	public MultiFileResource(final Path... files) {
		add(files);
	}

	/**
	 * 增加文件资源
	 *
	 * @param files 文件资源
	 * @return this
	 */
	public MultiFileResource add(final File... files) {
		for (final File file : files) {
			this.add(new FileResource(file));
		}
		return this;
	}

	/**
	 * 增加文件资源
	 *
	 * @param files 文件资源
	 * @return this
	 */
	public MultiFileResource add(final Path... files) {
		for (final Path file : files) {
			this.add(new FileResource(file));
		}
		return this;
	}

	/**
	 * 增加文件资源
	 *
	 * @param files 文件资源
	 * @return this
	 */
	public MultiFileResource add(final Collection<File> files) {
		for (final File file : files) {
			this.add(new FileResource(file));
		}
		return this;
	}

	@Override
	public MultiFileResource add(final Resource resource) {
		return (MultiFileResource) super.add(resource);
	}
}
