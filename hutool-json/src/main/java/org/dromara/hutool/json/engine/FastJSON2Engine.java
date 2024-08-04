/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.engine;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * FastJSON2引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class FastJSON2Engine implements JSONEngine {
	@Override
	public void serialize(final Object bean, final Writer writer) {
		final JSONWriter.Context context = new JSONWriter.Context();
		try (final JSONWriter jsonWriter = JSONWriter.ofUTF8(context)) {
			if (bean == null) {
				jsonWriter.writeNull();
			} else {
				jsonWriter.setRootObject(bean);
				final Class<?> valueClass = bean.getClass();
				final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
				objectWriter.write(jsonWriter, bean, null, null, 0);
			}

			jsonWriter.flushTo(writer);
		}
	}

	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		return JSON.parseObject(reader, (Type) type);
	}
}
