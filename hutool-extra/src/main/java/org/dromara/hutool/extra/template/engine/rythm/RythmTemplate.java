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

package org.dromara.hutool.extra.template.engine.rythm;

import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.extra.template.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Rythm模板包装
 *
 * @author looly
 *
 */
public class RythmTemplate implements Template, Serializable {
	private static final long serialVersionUID = -132774960373894911L;

	private final org.rythmengine.template.ITemplate rawTemplate;

	/**
	 * 包装Rythm模板
	 *
	 * @param template Rythm的模板对象 {@link org.rythmengine.template.ITemplate}
	 * @return {@code RythmTemplate}
	 */
	public static RythmTemplate wrap(final org.rythmengine.template.ITemplate template) {
		return (null == template) ? null : new RythmTemplate(template);
	}

	/**
	 * 构造
	 *
	 * @param rawTemplate Velocity模板对象
	 */
	public RythmTemplate(final org.rythmengine.template.ITemplate rawTemplate) {
		this.rawTemplate = rawTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = ConvertUtil.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.__setRenderArgs(map);
		rawTemplate.render(writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.__setRenderArgs(bindingMap);
		rawTemplate.render(out);
	}
}
