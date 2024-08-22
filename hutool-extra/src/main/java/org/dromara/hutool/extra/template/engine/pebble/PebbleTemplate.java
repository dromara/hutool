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

package org.dromara.hutool.extra.template.engine.pebble;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateException;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Pebble模板实现
 *
 * @author zooooooooy
 * @since 6.0.0
 */
public class PebbleTemplate implements Template {

	/**
	 * 包装pebbleTemplate模板
	 *
	 * @param template {@link io.pebbletemplates.pebble.template.PebbleTemplate}
	 * @return PebbleTemplate
	 */
	public static PebbleTemplate wrap(final io.pebbletemplates.pebble.template.PebbleTemplate template) {
		return (null == template) ? null : new PebbleTemplate(template);
	}

	private final io.pebbletemplates.pebble.template.PebbleTemplate template;

	/**
	 * 构造
	 *
	 * @param template {@link io.pebbletemplates.pebble.template.PebbleTemplate}
	 */
	public PebbleTemplate(final io.pebbletemplates.pebble.template.PebbleTemplate template) {
		this.template = template;
	}

	/**
	 * 渲染对象
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param writer     输出
	 */
	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {

		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {
		}, bindingMap);
		try {
			this.template.evaluate(writer, map);
		} catch (final Exception e) {
			throw new TemplateException("pebble template parse failed, cause by: ", e);
		}
	}

	/**
	 * 渲染对象
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param out        输出
	 */
	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {

		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {
		}, bindingMap);
		try {
			this.template.evaluate(new OutputStreamWriter(out), map);
		} catch (final Exception e) {
			throw new TemplateException("pebble template parse failed, cause by: ", e);
		}

	}

}
