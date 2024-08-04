/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.writer;

import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.serialize.JSONStringer;

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
