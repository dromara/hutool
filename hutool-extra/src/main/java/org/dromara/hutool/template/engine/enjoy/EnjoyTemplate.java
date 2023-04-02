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

package org.dromara.hutool.template.engine.enjoy;

import org.dromara.hutool.template.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Engoy模板实现
 *
 * @author looly
 * @since 4.1.9
 */
public class EnjoyTemplate implements Template, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装Enjoy模板
	 *
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 * @return {@code EnjoyTemplate}
	 */
	public static EnjoyTemplate wrap(final com.jfinal.template.Template EnjoyTemplate) {
		return (null == EnjoyTemplate) ? null : new EnjoyTemplate(EnjoyTemplate);
	}

	private final com.jfinal.template.Template rawTemplate;

	/**
	 * 构造
	 *
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 */
	public EnjoyTemplate(final com.jfinal.template.Template EnjoyTemplate) {
		this.rawTemplate = EnjoyTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		rawTemplate.render(bindingMap, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.render(bindingMap, out);
	}

}
