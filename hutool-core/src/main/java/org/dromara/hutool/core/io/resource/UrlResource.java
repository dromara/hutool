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

import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.net.url.UrlProtocolUtil;
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

	/**
	 * URL
	 */
	protected URL url;
	/**
	 * 资源名称
	 */
	protected String name;
	private long lastModified = 0;

	//-------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param uri URI
	 * @since 5.7.21
	 */
	public UrlResource(final URI uri) {
		this(UrlUtil.url(uri), null);
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
		if (null != url && UrlProtocolUtil.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
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
		return UrlUtil.size(this.url);
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		if (null == this.url) {
			throw new NoResourceException("Resource URL is null!");
		}
		return UrlUtil.getStream(url);
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
		return new UrlResource(UrlUtil.getURL(getUrl(), relativePath));
	}
}
