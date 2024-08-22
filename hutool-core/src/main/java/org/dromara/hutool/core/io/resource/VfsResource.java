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

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.io.File;
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
	private static final Method VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE;

	static {
		final Class<?> virtualFile = ClassLoaderUtil.loadClass(VFS3_PKG + "VirtualFile");
		try {
			VIRTUAL_FILE_METHOD_EXISTS = virtualFile.getMethod("exists");
			VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = virtualFile.getMethod("openStream");
			VIRTUAL_FILE_METHOD_GET_SIZE = virtualFile.getMethod("getSize");
			VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = virtualFile.getMethod("getLastModified");
			VIRTUAL_FILE_METHOD_TO_URL = virtualFile.getMethod("toURL");
			VIRTUAL_FILE_METHOD_GET_NAME = virtualFile.getMethod("getName");
			VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE = virtualFile.getMethod("getPhysicalFile");
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
	@Override
	public long size() {
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_SIZE);
	}

	/**
	 * 获取物理文件对象
	 *
	 * @return 物理文件对象
	 * @since 6.0.0
	 */
	public File getFile(){
		return MethodUtil.invoke(virtualFile, VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE);
	}

}
