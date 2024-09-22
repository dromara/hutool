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

package org.dromara.hutool.json.engine.gson;

import com.google.gson.*;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;

/**
 * 时间相关对象序列化描述
 *
 * @author Looly
 * @since 6.0.0
 */
public class TemporalGsonTypeAdapter implements GsonTypeAdapter<TemporalAccessor> {

	private final Class<? extends TemporalAccessor> type;
	private final String dateFormat;

	/**
	 * 构造
	 *
	 * @param type       时间类型
	 * @param dateFormat 日期格式
	 */
	public TemporalGsonTypeAdapter(final Class<? extends TemporalAccessor> type, final String dateFormat) {
		this.type = type;
		this.dateFormat = dateFormat;
	}


	@Override
	public JsonElement serialize(final TemporalAccessor src, final Type typeOfSrc, final JsonSerializationContext context) {
		return StrUtil.isEmpty(dateFormat) ?
			new JsonPrimitive(TimeUtil.toEpochMilli(src)) :
			new JsonPrimitive(TimeUtil.format(src, dateFormat));
	}

	@Override
	public TemporalAccessor deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return StrUtil.isEmpty(dateFormat) ?
			ConvertUtil.convert(this.type, json.getAsLong()) :
			ConvertUtil.convert(this.type, TimeUtil.parse(json.getAsString(), dateFormat));
	}
}
