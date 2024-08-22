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

package org.dromara.hutool.extra.template.engine.wit;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.reflect.TypeReference;
import org.febit.wit.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Wit模板实现
 *
 * @author looly
 */
public class WitTemplate implements org.dromara.hutool.extra.template.Template, Serializable{
	private static final long serialVersionUID = 1L;

	private final Template rawTemplate;

	/**
	 * 包装Wit模板
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 * @return WitTemplate
	 */
	public static WitTemplate wrap(final Template witTemplate) {
		return (null == witTemplate) ? null : new WitTemplate(witTemplate);
	}

	/**
	 * 构造
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 */
	public WitTemplate(final Template witTemplate) {
		this.rawTemplate = witTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, out);
	}

}
