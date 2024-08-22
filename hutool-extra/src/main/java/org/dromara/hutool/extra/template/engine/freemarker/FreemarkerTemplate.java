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

package org.dromara.hutool.extra.template.engine.freemarker;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Freemarker模板实现
 *
 * @author looly
 */
public class FreemarkerTemplate implements Template, Serializable{
	private static final long serialVersionUID = -8157926902932567280L;

	freemarker.template.Template rawTemplate;

	/**
	 * 包装Freemarker模板
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link freemarker.template.Template}
	 * @return this
	 */
	public static FreemarkerTemplate wrap(final freemarker.template.Template beetlTemplate) {
		return (null == beetlTemplate) ? null : new FreemarkerTemplate(beetlTemplate);
	}

	/**
	 * 构造
	 *
	 * @param freemarkerTemplate Beetl的模板对象 {@link freemarker.template.Template}
	 */
	public FreemarkerTemplate(final freemarker.template.Template freemarkerTemplate) {
		this.rawTemplate = freemarkerTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		try {
			rawTemplate.process(bindingMap, writer);
		} catch (final freemarker.template.TemplateException e) {
			throw new TemplateException(e);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		render(bindingMap, IoUtil.toWriter(out, Charset.forName(this.rawTemplate.getEncoding())));
	}

}
