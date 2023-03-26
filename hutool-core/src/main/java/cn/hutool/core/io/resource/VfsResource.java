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

package cn.hutool.core.io.resource;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.reflect.MethodUtil;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * VFS资源封装<br>
 * 支持VFS 3.x on JBoss AS 6+，JBoss AS 7 and WildFly 8+<br>
 * 参考：org.springframework.core.io.VfsUtils
 *
 * @author looly, Spring
 * @since 5.7.21
 */
public class VfsResource implements Resource {
	private static final String VFS3_PKG = "org.jboss.vfs.";

	private static final Method VIRTUAL_FILE_METHOD_EXISTS;
	private static final Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
	private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;
	private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
	private static final Method VIRTUAL_FILE_METHOD_TO_URL;
	private static final Method VIRTUAL_FILE_METHOD_GET_NAME;

	static {
		final Class<?> virtualFile = ClassLoaderUtil.loadClass(VFS3_PKG + "VirtualFile");
		try {
			VIRTUAL_FILE_METHOD_EXISTS = virtualFile.getMethod("exists");
			VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = virtualFile.getMethod("openStream");
			VIRTUAL_FILE_METHOD_GET_SIZE = virtualFile.getMethod("getSize");
			VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = virtualFile.getMethod("getLastModified");
			VIRTUAL_FILE_METHOD_TO_URL = virtualFile.getMethod("toURL");
			VIRTUAL_FILE_METHOD_GET_NAME = virtualFile.getMethod("getName");
		} catch (final NoSuchMethodException ex) {
			throw new IllegalStateException("Could not detect JBoss VFS infrastructure", ex);
		}
	}

	/**
	 * org.jboss.vfs.VirtualFile实例对象
	 */
	private final Object virtualFile;
	private final long lastModified;

	/**
	 * 构造
	 *
	 * @param resource org.jboss.vfs.VirtualFile实例对象
	 */
	public VfsResource(final Object resource) {
		Assert.notNull(resource, "VirtualFile must not be null");
		this.virtualFile = resource;
		this.lastModified = getLastModified();
	}

	/**
	 * VFS文件是否存在
	 *
	 * @return 文件是否存在
	 */
	public boolean exists() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_EXISTS);
	}

	@Override
	public String getName() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_NAME);
	}

	@Override
	public URL getUrl() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_TO_URL);
	}

	@Override
	public InputStream getStream() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_INPUT_STREAM);
	}

	@Override
	public boolean isModified() {
		return this.lastModified != getLastModified();
	}

	/**
	 * 获得VFS文件最后修改时间
	 *
	 * @return 最后修改时间
	 */
	public long getLastModified() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED);
	}

	/**
	 * 获取VFS文件大小
	 *
	 * @return VFS文件大小
	 */
	public long size() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_SIZE);
	}

}
