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

package org.dromara.hutool.extra.template.engine.jetbrick;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.extra.template.Template;
import jetbrick.template.JetTemplate;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Jetbrick模板实现<br>
 * 见：https://github.com/subchen/jetbrick-template-2x
 *
 * @author looly
 * @since 5.7.21
 */
public class JetbrickTemplate implements Template, Serializable{
	private static final long serialVersionUID = 1L;

	private final JetTemplate rawTemplate;

	/**
	 * 包装Jetbrick模板
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 * @return JetbrickTemplate
	 */
	public static JetbrickTemplate wrap(final JetTemplate jetTemplate) {
		return (null == jetTemplate) ? null : new JetbrickTemplate(jetTemplate);
	}

	/**
	 * 构造
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 */
	public JetbrickTemplate(final JetTemplate jetTemplate) {
		this.rawTemplate = jetTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.render(map, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.render(map, out);
	}

}
