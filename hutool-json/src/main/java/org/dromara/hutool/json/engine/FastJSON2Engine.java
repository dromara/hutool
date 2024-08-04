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

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
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

	private final JSONWriter.Context writerContext;
	private final JSONReader.Context readerContext;

	/**
	 * 构造
	 */
	public FastJSON2Engine() {
		this.writerContext = JSONFactory.createWriteContext();
		this.readerContext = JSONFactory.createReadContext();
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		try (final JSONWriter jsonWriter = JSONWriter.of(this.writerContext)) {
			if (bean == null) {
				jsonWriter.writeNull();
			} else {
				jsonWriter.setRootObject(bean);
				final Class<?> valueClass = bean.getClass();
				final ObjectWriter<?> objectWriter = this.writerContext.getObjectWriter(valueClass, valueClass);
				objectWriter.write(jsonWriter, bean, null, null, 0);
			}

			jsonWriter.flushTo(writer);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		final ObjectReader<T> objectReader = this.readerContext.getObjectReader((Type) type);

		try (final JSONReader jsonReader = JSONReader.of(reader, this.readerContext)) {
			if (jsonReader.isEnd()) {
				return null;
			}

			final T object = objectReader.readObject(jsonReader, (Type) type, null, 0);
			jsonReader.handleResolveTasks(object);
			return object;
		}
	}
}
