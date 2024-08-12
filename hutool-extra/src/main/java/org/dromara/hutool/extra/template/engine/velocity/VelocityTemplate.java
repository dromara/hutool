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
