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

package org.dromara.hutool.io.resource;

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
