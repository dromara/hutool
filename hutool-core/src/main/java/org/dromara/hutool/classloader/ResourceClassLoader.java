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

package org.dromara.hutool.classloader;

import org.dromara.hutool.io.resource.Resource;
import org.dromara.hutool.util.ObjUtil;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源类加载器，可以加载任意类型的资源类
 *
 * @param <T> {@link Resource}接口实现类
 * @author looly, lzpeng
 * @since 5.5.2
 */
public class ResourceClassLoader<T extends Resource> extends SecureClassLoader {

	private final Map<String, T> resourceMap;
	/**
	 * 缓存已经加载的类
	 */
	private final Map<String, Class<?>> cacheClassMap;

	/**
	 * 构造
	 *
	 * @param parentClassLoader 父类加载器，null表示默认当前上下文加载器
	 * @param resourceMap       资源map
	 */
	public ResourceClassLoader(final ClassLoader parentClassLoader, final Map<String, T> resourceMap) {
		super(ObjUtil.defaultIfNull(parentClassLoader, ClassLoaderUtil::getClassLoader));
		this.resourceMap = ObjUtil.defaultIfNull(resourceMap, HashMap::new);
		this.cacheClassMap = new HashMap<>();
	}

	/**
	 * 增加需要加载的类资源
	 *
	 * @param resource 资源，可以是文件、流或者字符串
	 * @return this
	 */
	public ResourceClassLoader<T> addResource(final T resource) {
		this.resourceMap.put(resource.getName(), resource);
		return this;
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		final Class<?> clazz = cacheClassMap.computeIfAbsent(name, this::defineByName);
		if (clazz == null) {
			return super.findClass(name);
		}
		return clazz;
	}

	/**
	 * 从给定资源中读取class的二进制流，然后生成类<br>
	 * 如果这个类资源不存在，返回{@code null}
	 *
	 * @param name 类名
	 * @return 定义的类
	 */
	private Class<?> defineByName(final String name) {
		final Resource resource = resourceMap.get(name);
		if (null != resource) {
			final byte[] bytes = resource.readBytes();
			return defineClass(name, bytes, 0, bytes.length);
		}
		return null;
	}
}
