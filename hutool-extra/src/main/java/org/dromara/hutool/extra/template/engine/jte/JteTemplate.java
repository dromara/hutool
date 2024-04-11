/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.template.engine.jte;

import gg.jte.TemplateEngine;
import gg.jte.output.WriterOutput;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.extra.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

	public JteTemplate(TemplateEngine engine, String template) {
		this.templateEngine = engine;
		this.template = template;
	}

	@Override
	public void render(Map<?, ?> bindingMap, Writer writer) {
		templateEngine.render(template, bindingMap, new WriterOutput(writer));
	}

	@Override
	public void render(Map<?, ?> bindingMap, OutputStream out) {
		this.render(bindingMap, IoUtil.toWriter(out, StandardCharsets.UTF_8));
	}

	/**
	 * 将模板与绑定参数融合后输出到Writer
	 *
	 * @param model  实体类
	 * @param writer 输出
	 */
	public void render(Object model, Writer writer) {
		templateEngine.render(template, model, new WriterOutput(writer));
	}

	/**
	 * 将模板与绑定参数融合后输出到流
	 *
	 * @param model 实体类
	 * @param out   输出
	 */
	public void render(Object model, OutputStream out) {
		render(model, IoUtil.toWriter(out, StandardCharsets.UTF_8));
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
