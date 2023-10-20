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

import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.URLUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 * URL资源访问类
 *
 * @author Looly
 */
public class UrlResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	protected URL url;
	private long lastModified = 0;
	protected String name;

	//-------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param uri URI
	 * @since 5.7.21
	 */
	public UrlResource(final URI uri) {
		this(URLUtil.url(uri), null);
	}

	/**
	 * 构造
	 *
	 * @param url URL
	 */
	public UrlResource(final URL url) {
		this(url, null);
	}

	/**
	 * 构造
	 *
	 * @param url  URL，允许为空
	 * @param name 资源名称
	 */
	public UrlResource(final URL url, final String name) {
		this.url = url;
		if (null != url && URLUtil.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
			this.lastModified = FileUtil.file(url).lastModified();
		}
		this.name = ObjUtil.defaultIfNull(name, () -> (null != url ? FileNameUtil.getName(url.getPath()) : null));
	}

	//-------------------------------------------------------------------------------------- Constructor end

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return this.url;
	}

	@Override
	public long size() {
		return URLUtil.size(this.url);
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		if (null == this.url) {
			throw new NoResourceException("Resource URL is null!");
		}
		return URLUtil.getStream(url);
	}

	@Override
	public boolean isModified() {
		// lastModified == 0表示此资源非文件资源
		return (0 != this.lastModified) && this.lastModified != getFile().lastModified();
	}

	/**
	 * 获得File
	 *
	 * @return {@link File}
	 */
	public File getFile() {
		return FileUtil.file(this.url);
	}

	/**
	 * 返回路径
	 *
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return (null == this.url) ? "null" : this.url.toString();
	}

	/**
	 * 获取相对于本资源的资源
	 *
	 * @param relativePath 相对路径
	 * @return 子资源
	 * @since 6.0.0
	 */
	public UrlResource createRelative(final String relativePath) {
		return new UrlResource(URLUtil.getURL(getUrl(), relativePath));
	}
}
