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

import org.dromara.hutool.core.lang.Assert;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * HTTP资源，用于自定义表单数据，可自定义Content-Type
 *
 * @author looly
 * @since 5.7.17
 */
public class HttpResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final Resource resource;
	private final String contentType;

	/**
	 * 构造
	 *
	 * @param resource    资源，非空
	 * @param contentType Content-Type类型，{@code null}表示不设置
	 */
	public HttpResource(final Resource resource, final String contentType) {
		this.resource = Assert.notNull(resource, "Resource must be not null !");
		this.contentType = contentType;
	}

	@Override
	public String getName() {
		return resource.getName();
	}

	@Override
	public URL getUrl() {
		return resource.getUrl();
	}

	@Override
	public long size() {
		return resource.size();
	}

	@Override
	public InputStream getStream() {
		return resource.getStream();
	}

	/**
	 * 获取自定义Content-Type类型
	 *
	 * @return Content-Type类型
	 */
	public String getContentType() {
		return this.contentType;
	}
}
