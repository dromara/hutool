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

package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.net.URL;

/**
 * ClassPath单一资源访问类<br>
 * 传入路径path必须为相对路径，如果传入绝对路径，Linux路径会去掉开头的“/”，而Windows路径会直接报错。<br>
 * 传入的path所指向的资源必须存在，否则报错
 *
 * @author Looly
 *
 */
public class ClassPathResource extends UrlResource {
	private static final long serialVersionUID = 1L;

	private final String path;
	private final ClassLoader classLoader;
	private final Class<?> clazz;

	// -------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 *
	 * @param path 相对于ClassPath的路径
	 */
	public ClassPathResource(final String path) {
		this(path, null, null);
	}

	/**
	 * 构造
	 *
	 * @param path 相对于ClassPath的路径
	 * @param classLoader {@link ClassLoader}
	 */
	public ClassPathResource(final String path, final ClassLoader classLoader) {
		this(path, classLoader, null);
	}

	/**
	 * 构造
	 *
	 * @param path 相对于给定Class的路径
	 * @param clazz {@link Class} 用于定位路径
	 */
	public ClassPathResource(final String path, final Class<?> clazz) {
		this(path, null, clazz);
	}

	/**
	 * 构造
	 *
	 * @param pathBaseClassLoader 相对路径
	 * @param classLoader {@link ClassLoader}
	 * @param clazz {@link Class} 用于定位路径
	 */
	public ClassPathResource(final String pathBaseClassLoader, final ClassLoader classLoader, final Class<?> clazz) {
		super((URL) null);
		Assert.notNull(pathBaseClassLoader, "Path must not be null");

		final String path = normalizePath(pathBaseClassLoader);
		this.path = path;
		this.name = StrUtil.isBlank(path) ? null : FileNameUtil.getName(path);

		this.classLoader = ObjUtil.defaultIfNull(classLoader, ClassLoaderUtil::getClassLoader);
		this.clazz = clazz;
		initUrl();
	}
	// -------------------------------------------------------------------------------------- Constructor end

	/**
	 * 获得Path
	 *
	 * @return path
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * 获得绝对路径Path<br>
	 * 对于不存在的资源，返回拼接后的绝对路径
	 *
	 * @return 绝对路径path
	 */
	public final String getAbsolutePath() {
		if (FileUtil.isAbsolutePath(this.path)) {
			return this.path;
		}
		// url在初始化的时候已经断言，此处始终不为null
		return FileUtil.normalize(UrlUtil.getDecodedPath(this.url));
	}

	/**
	 * 获得 {@link ClassLoader}
	 *
	 * @return {@link ClassLoader}
	 */
	public final ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * 根据给定资源初始化URL
	 */
	private void initUrl() {
		if (null != this.clazz) {
			super.url = this.clazz.getResource(this.path);
		} else if (null != this.classLoader) {
			super.url = this.classLoader.getResource(this.path);
		} else {
			super.url = ClassLoader.getSystemResource(this.path);
		}
		if (null == super.url) {
			throw new NoResourceException("Resource of path [{}] not exist!", this.path);
		}
	}

	@Override
	public String toString() {
		return (null == this.path) ? super.toString() : "classpath:" + this.path;
	}

	/**
	 * 标准化Path格式
	 *
	 * @param path Path
	 * @return 标准化后的path
	 */
	private String normalizePath(String path) {
		// 标准化路径
		path = FileUtil.normalize(path);
		path = StrUtil.removePrefix(path, StrUtil.SLASH);

		Assert.isFalse(FileUtil.isAbsolutePath(path), "Path [{}] must be a relative path !", path);
		return path;
	}
}
