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

package org.dromara.hutool.extra.template.engine.freemarker;

import freemarker.cache.TemplateLoader;

import java.io.Reader;
import java.io.StringReader;

/**
 * {@link TemplateLoader} 字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author looly
 * @since 4.3.3
 */
public class SimpleStringTemplateLoader implements TemplateLoader {

	@Override
	public Object findTemplateSource(final String name) {
		return name;
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return System.currentTimeMillis();
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) {
		return new StringReader((String) templateSource);
	}

	@Override
	public void closeTemplateSource(final Object templateSource) {
		// ignore
	}

}
