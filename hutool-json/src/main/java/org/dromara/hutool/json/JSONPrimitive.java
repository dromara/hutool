/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.json.writer.JSONWriter;
import org.dromara.hutool.json.writer.ValueWriter;
import org.dromara.hutool.json.writer.ValueWriterManager;

import java.lang.reflect.Type;

/**
 * JSON原始类型数据封装，根据RFC8259规范，JSONPrimitive只包含以下类型：
 * <ul>
 *     <li>number</li>
 *     <li>string</li>
 *     <li>null</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONPrimitive implements JSON {
	private static final long serialVersionUID = -2026215279191790345L;

	private Object value;
	/**
	 * 配置项
	 */
	private JSONConfig config;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public JSONPrimitive(final Object value) {
		this(value, null);
	}

	/**
	 * 构造
	 *
	 * @param value  值
	 * @param config 配置项
	 */
	public JSONPrimitive(final Object value, final JSONConfig config) {
		this.value = Assert.notNull(value);
		this.config = config;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * 设置值
	 *
	 * @param value 值
	 * @return this
	 */
	public JSONPrimitive setValue(final Object value) {
		this.value = value;
		return this;
	}

	@Override
	public JSONConfig config() {
		return this.config;
	}

	/**
	 * 设置配置项
	 *
	 * @param config 配置项
	 * @return this
	 */
	JSONPrimitive setConfig(final JSONConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public <T> T convert(final Type targetType, final Object value, final T defaultValue) {
		return JSON.super.convert(targetType, this.value, defaultValue);
	}

	@Override
	public void write(final JSONWriter writer) throws JSONException {
		// 自定义规则
		final ValueWriter valueWriter = ValueWriterManager.getInstance().get(value);
		if (null != valueWriter) {
			valueWriter.write(writer, value);
			return;
		}

		// 默认包装字符串
		writer.writeQuoteStrValue(value.toString());
	}

	@Override
	public String toString() {
		final JSONWriter jsonWriter = JSONWriter.of(new StringBuilder(), 0, 0, this.config);
		write(jsonWriter);
		return jsonWriter.toString();
	}
}
