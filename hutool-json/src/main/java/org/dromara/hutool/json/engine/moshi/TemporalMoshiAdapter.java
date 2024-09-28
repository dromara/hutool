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

package org.dromara.hutool.json.engine.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;

/**
 * 时间类型适配器，用于处理时间类型的序列化与反序列化
 *
 * @author looly
 * @since 6.0.0
 */
public class TemporalMoshiAdapter extends JsonAdapter<TemporalAccessor> {

	private final Class<? extends TemporalAccessor> type;
	private final String dateFormat;

	/**
	 * 构造
	 *
	 * @param type       时间类型
	 * @param dateFormat 日期格式
	 */
	public TemporalMoshiAdapter(final Class<? extends TemporalAccessor> type, final String dateFormat) {
		this.type = type;
		this.dateFormat = dateFormat;
	}

	@Override
	public void toJson(final JsonWriter jsonWriter, final TemporalAccessor src) throws IOException {
		if (null == src){
			jsonWriter.nullValue();
			return;
		}

		if (StrUtil.isEmpty(dateFormat)) {
			jsonWriter.value(TimeUtil.toEpochMilli(src));
		} else {
			jsonWriter.value(TimeUtil.format(src, dateFormat));
		}
	}

	@Override
	public TemporalAccessor fromJson(final JsonReader jsonReader) throws IOException {
		if(jsonReader.peek() == JsonReader.Token.NULL){
			return jsonReader.nextNull();
		}
		return StrUtil.isEmpty(dateFormat) ?
			ConvertUtil.convert(this.type, jsonReader.nextLong()) :
			ConvertUtil.convert(this.type, TimeUtil.parse(jsonReader.nextString(), dateFormat));
	}
}
