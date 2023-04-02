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

package org.dromara.hutool.template.engine.thymeleaf;

import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.reflect.TypeReference;
import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.util.ObjUtil;
import org.dromara.hutool.template.Template;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

/**
 * Thymeleaf模板实现
 *
 * @author looly
 * @since 4.1.11
 */
public class ThymeleafTemplate implements Template, Serializable {
	private static final long serialVersionUID = 781284916568562509L;

	private final TemplateEngine engine;
	private final String template;
	private final Charset charset;

	/**
	 * 包装Thymeleaf模板
	 *
	 * @param engine Thymeleaf的模板引擎对象 {@link TemplateEngine}
	 * @param template 模板路径或模板内容
	 * @param charset 编码
	 * @return {@code ThymeleafTemplate}
	 */
	public static ThymeleafTemplate wrap(final TemplateEngine engine, final String template, final Charset charset) {
		return (null == engine) ? null : new ThymeleafTemplate(engine, template, charset);
	}

	/**
	 * 构造
	 *
	 * @param engine Thymeleaf的模板对象 {@link TemplateEngine}
	 * @param template 模板路径或模板内容
	 * @param charset 编码
	 */
	public ThymeleafTemplate(final TemplateEngine engine, final String template, final Charset charset) {
		this.engine = engine;
		this.template = template;
		this.charset = ObjUtil.defaultIfNull(charset, CharsetUtil.UTF_8);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		final Context context = new Context(Locale.getDefault(), map);
		this.engine.process(this.template, context, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		render(bindingMap, IoUtil.toWriter(out, this.charset));
	}

}
