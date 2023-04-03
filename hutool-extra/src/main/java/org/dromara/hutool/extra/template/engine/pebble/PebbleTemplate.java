/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
