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

package org.dromara.hutool.extra.template.engine.velocity;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

/**
 * {@link ResourceLoader} 的字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author looly
 *
 */
public class SimpleStringResourceLoader extends ResourceLoader {

	@Override
	public void init(final ExtProperties configuration) {
	}

	/**
	 * 获取资源流
	 *
	 * @param source 字符串模板
	 * @return 流
	 * @throws ResourceNotFoundException 资源未找到
	 */
	public InputStream getResourceStream(final String source) throws ResourceNotFoundException {
		return IoUtil.toStream(source, CharsetUtil.UTF_8);
	}

	@Override
	public Reader getResourceReader(final String source, final String encoding) throws ResourceNotFoundException {
		return new StringReader(source);
	}

	@Override
	public boolean isSourceModified(final Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(final Resource resource) {
		return 0;
	}

}
