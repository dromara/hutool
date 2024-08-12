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

package org.dromara.hutool.extra.template.engine.beetl;

import org.dromara.hutool.extra.template.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Beetl模板实现
 *
 * @author looly
 */
public class BeetlTemplate implements Template, Serializable{
	private static final long serialVersionUID = -8157926902932567280L;

	private final org.beetl.core.Template rawTemplate;

	/**
	 * 包装Beetl模板
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 * @return BeetlTemplate
	 */
	public static BeetlTemplate wrap(final org.beetl.core.Template beetlTemplate) {
		return (null == beetlTemplate) ? null : new BeetlTemplate(beetlTemplate);
	}

	/**
	 * 构造
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 */
	public BeetlTemplate(final org.beetl.core.Template beetlTemplate) {
		this.rawTemplate = beetlTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		rawTemplate.binding(bindingMap);
		rawTemplate.renderTo(writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.binding(bindingMap);
		rawTemplate.renderTo(out);
	}

}
