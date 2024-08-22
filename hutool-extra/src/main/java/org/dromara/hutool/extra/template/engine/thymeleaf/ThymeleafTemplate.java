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

package org.dromara.hutool.extra.template.engine.thymeleaf;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.extra.template.Template;
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
