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

package org.dromara.hutool.extra.template.engine.jetbrick.loader;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import jetbrick.io.resource.AbstractResource;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.loader.AbstractResourceLoader;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 字符串模板加载器
 *
 * @author looly
 * @since 5.7.21
 */
public class StringResourceLoader extends AbstractResourceLoader {

	private Charset charset;

	@Override
	public Resource load(final String name) {
		return new StringTemplateResource(name, charset);
	}

	/**
	 * 设置编码
	 * @param charset 编码
	 */
	public void setCharset(final Charset charset){
		this.charset = charset;
	}

	/**
	 * 字符串模板
	 *
	 * @author looly
	 */
	static class StringTemplateResource extends AbstractResource {

		private final String content;
		private final Charset charset;

		/**
		 * 构造
		 * @param content 模板内容
		 * @param charset 编码
		 */
		public StringTemplateResource(final String content, final Charset charset){
			this.content = content;
			this.charset = charset;
		}

		@Override
		public InputStream openStream() throws ResourceNotFoundException {
			return IoUtil.toStream(content, charset);
		}

		@Override
		public URL getURL() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean exist() {
			return StrUtil.isEmpty(content);
		}

		@Override
		public String toString() {
			return this.content;
		}

		@Override
		public long lastModified() {
			return 1;
		}
	}
}
