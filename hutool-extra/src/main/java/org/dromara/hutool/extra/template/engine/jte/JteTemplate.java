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

import gg.jte.TemplateEngine;
import gg.jte.output.WriterOutput;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.extra.template.Template;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Jte模板实现
 *
 * @author dy
 */
public class JteTemplate implements Template, Serializable {

	private static final long serialVersionUID = -2739915422007257186L;

	private final TemplateEngine templateEngine;
	private final String template;
	private final Charset charset;

	/**
	 * 构造
	 *
	 * @param engine   jet引擎
	 * @param template 模板
	 * @param charset 输出编码
	 */
	public JteTemplate(final TemplateEngine engine, final String template, final Charset charset) {
		this.templateEngine = engine;
		this.template = template;
		this.charset = charset;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		templateEngine.render(template, bindingMap, new WriterOutput(writer));
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		this.render(bindingMap, IoUtil.toWriter(out, charset));
	}

	/**
	 * 将模板与绑定参数融合后输出到Writer
	 *
	 * @param model  实体类
	 * @param writer 输出
	 */
	public void render(final Object model, final Writer writer) {
		templateEngine.render(template, model, new WriterOutput(writer));
	}

	/**
	 * 将模板与绑定参数融合后输出到流
	 *
	 * @param model 实体类
	 * @param out   输出
	 */
	public void render(final Object model, final OutputStream out) {
		render(model, IoUtil.toWriter(out, charset));
	}

	/**
	 * 写出到文件
	 *
	 * @param model 实体类
	 * @param file  输出到的文件
	 */
	public void render(final Object model, final File file) {
		BufferedOutputStream out = null;
		try {
			out = FileUtil.getOutputStream(file);
			this.render(model, out);
		} finally {
			IoUtil.closeQuietly(out);
		}
	}

	/**
	 * 将模板与绑定参数融合后返回为字符串
	 *
	 * @param model 实体类
	 * @return 融合后的内容
	 */
	public String render(final Object model) {
		final StringWriter writer = new StringWriter();
		this.render(model, writer);
		return writer.toString();
	}
}
