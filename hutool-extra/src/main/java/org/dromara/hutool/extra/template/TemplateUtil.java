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
