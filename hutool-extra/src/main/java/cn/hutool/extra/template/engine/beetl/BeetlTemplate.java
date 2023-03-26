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

package cn.hutool.extra.template.engine.beetl;

import cn.hutool.extra.template.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Beetl模板实现
 *
 * @author looly
 */
public class BeetlTemplate implements Template, Serializable{
	private static final long serialVersionUID = -8157926902932567280L;

	private final org.beetl.core.Template rawTemplate;

	/**
	 * 包装Beetl模板
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 * @return BeetlTemplate
	 */
	public static BeetlTemplate wrap(final org.beetl.core.Template beetlTemplate) {
		return (null == beetlTemplate) ? null : new BeetlTemplate(beetlTemplate);
	}

	/**
	 * 构造
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 */
	public BeetlTemplate(final org.beetl.core.Template beetlTemplate) {
		this.rawTemplate = beetlTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		rawTemplate.binding(bindingMap);
		rawTemplate.renderTo(writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.binding(bindingMap);
		rawTemplate.renderTo(out);
	}

}
