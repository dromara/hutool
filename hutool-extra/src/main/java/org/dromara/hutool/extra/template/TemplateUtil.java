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

package org.dromara.hutool.extra.template;

import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.engine.TemplateEngineFactory;

import java.io.Writer;
import java.util.Map;

/**
 * 提供模板工具类，用于快捷模板融合
 *
 * @author looly
 */
public class TemplateUtil {

	/**
	 * 获取单例的模板引擎
	 *
	 * @return {@link TemplateEngine}
	 */
	public static TemplateEngine getEngine() {
		return TemplateEngineFactory.getEngine();
	}

	/**
	 * 融合模板和参数，返回融合后的内容
	 *
	 * @param templateContent 模板内容
	 * @param bindingMap      参数
	 * @return 内容
	 */
	public static String render(final String templateContent, final Map<?, ?> bindingMap) {
		return getEngine().getTemplate(templateContent).render(bindingMap);
	}

	/**
	 * 融合模板和参数，返回融合后的内容
	 *
	 * @param templateContent 模板内容
	 * @param bindingMap      参数
	 * @param writer          融合内容输出的位置
	 */
	public static void render(final String templateContent, final Map<?, ?> bindingMap, final Writer writer) {
		getEngine().getTemplate(templateContent).render(bindingMap, writer);
	}
}
