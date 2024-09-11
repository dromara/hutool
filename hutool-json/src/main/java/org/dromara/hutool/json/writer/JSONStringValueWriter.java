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

package org.dromara.hutool.json.writer;

import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.serializer.JSONStringer;

/**
 * {@link JSONStringer}的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONStringValueWriter implements JSONValueWriter {
	/**
	 * 单例对象
	 */
	public static final JSONStringValueWriter INSTANCE = new JSONStringValueWriter();

	@Override
	public boolean test(final Object value) {
		return value instanceof JSONStringer;
	}

	/**
	 * 输出实现了{@link JSONStringer}接口的对象，通过调用{@link JSONStringer#toJSONString()}获取JSON字符串<br>
	 * {@link JSONStringer}按照JSON对象对待，此方法输出的JSON字符串不包装引号。<br>
	 * 如果toJSONString()返回null，调用toString()方法并使用双引号包装。
	 *
	 * @param writer {@link JSONWriter}
	 * @param jsonStringer {@link JSONStringer}
	 */
	@Override
	public void write(final JSONWriter writer, final Object jsonStringer) {
		final String valueStr;
		try {
			valueStr = ((JSONStringer)jsonStringer).toJSONString();
		} catch (final Exception e) {
			throw new JSONException(e);
		}
		if (null != valueStr) {
			writer.writeRaw(valueStr);
		} else {
			writer.writeQuoteStrValue(jsonStringer.toString());
		}
	}
}
