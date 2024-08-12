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

package org.dromara.hutool.extra.template.engine.jte;

import gg.jte.CodeResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link CodeResolver} 字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author cdy
 * @since 6.0.0
 */
public class SimpleStringCodeResolver implements CodeResolver {

	private final Map<String, String> templates;

	/**
	 * 构造
	 * @param templates 参数
	 */
	public SimpleStringCodeResolver(final Map<String, String> templates) {
		this.templates = templates;
	}

	@Override
	public String resolve(final String name) {
		return templates.get(name);
	}

	@Override
	public long getLastModified(final String name) {
		return 0L;
	}

	@Override
	public List<String> resolveAllTemplateNames() {
		return new ArrayList<>(templates.keySet());
	}

}
