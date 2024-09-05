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

package org.dromara.hutool.json.engine.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;

/**
 * Jackson处理{@link TemporalAccessor}相关对象序列化描述
 *
 * @author Looly
 * @since 6.0.0
 */
public class JacksonTemporalSerializer extends StdSerializer<TemporalAccessor> {
	private static final long serialVersionUID = 1L;

	private final String format;

	/**
	 * 构造
	 *
	 * @param format 日期格式，null表示使用时间戳
	 */
	public JacksonTemporalSerializer(final String format) {
		super(TemporalAccessor.class);
		this.format = format;
	}

	@Override
	public void serialize(final TemporalAccessor value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
		if (StrUtil.isEmpty(this.format)) {
			final long epochMilli = TimeUtil.toEpochMilli(value);
			gen.writeNumber(epochMilli);
		} else {
			gen.writeString(TimeUtil.format(value, this.format));
		}
	}
}
