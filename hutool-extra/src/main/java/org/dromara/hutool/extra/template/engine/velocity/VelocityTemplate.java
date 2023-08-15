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

package org.dromara.hutool.extra.template.engine.velocity;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.template.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Velocity模板包装
 *
 * @author looly
 *
 */
public class VelocityTemplate implements Template, Serializable {
	private static final long serialVersionUID = -132774960373894911L;

	private final org.apache.velocity.Template rawTemplate;
	private String charset;

	/**
	 * 包装Velocity模板
	 *
	 * @param template Velocity的模板对象 {@link org.apache.velocity.Template}
	 * @return VelocityTemplate
	 */
	public static VelocityTemplate wrap(final org.apache.velocity.Template template) {
		return (null == template) ? null : new VelocityTemplate(template);
	}

	/**
	 * 构造
	 *
	 * @param rawTemplate Velocity模板对象
	 */
	public VelocityTemplate(final org.apache.velocity.Template rawTemplate) {
		this.rawTemplate = rawTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		rawTemplate.merge(toContext(bindingMap), writer);
		IoUtil.flush(writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		if(null == charset) {
			loadEncoding();
		}
		render(bindingMap, IoUtil.toWriter(out, CharsetUtil.charset(this.charset)));
	}

	/**
	 * 将Map转为VelocityContext
	 *
	 * @param bindingMap 参数绑定的Map
	 * @return {@link VelocityContext}
	 */
	private VelocityContext toContext(final Map<?, ?> bindingMap) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		return new VelocityContext(map);
	}

	/**
	 * 加载可用的Velocity中预定义的编码
	 */
	private void loadEncoding() {
		final String charset = (String) Velocity.getProperty(Velocity.INPUT_ENCODING);
		this.charset = StrUtil.isEmpty(charset) ? CharsetUtil.NAME_UTF_8 : charset;
	}
}
