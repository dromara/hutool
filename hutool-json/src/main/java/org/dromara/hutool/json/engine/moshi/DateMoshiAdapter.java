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
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 日期格式化适配器，用于moshi序列化与反序列化日期
 *
 * @author looly
 * @since 6.0.0
 */
public class DateMoshiAdapter extends JsonAdapter<Date> {

	/**
	 * 创建工厂
	 *
	 * @param dateFormat 日期格式
	 * @return 创建工厂
	 */
	public static Factory createFactory(final String dateFormat){
		return (type, set, moshi) -> {
			if(Date.class.isAssignableFrom(TypeUtil.getClass(type))){
				return new DateMoshiAdapter(dateFormat);
			}

			return null;
		};
	}

	private final String dateFormat;

	/**
	 * 构造
	 *
	 * @param dateFormat 日期格式
	 */
	public DateMoshiAdapter(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public void toJson(final JsonWriter jsonWriter, final Date date) throws IOException {
		if(null == date){
			jsonWriter.nullValue();
			return;
		}

		if(StrUtil.isEmpty(dateFormat)){
			jsonWriter.value(date.getTime());
		}else{
			jsonWriter.value(DateUtil.format(date, dateFormat));
		}
	}

	@Override
	public Date fromJson(final JsonReader jsonReader) throws IOException {
		if(jsonReader.peek() == JsonReader.Token.NULL){
			return jsonReader.nextNull();
		}

		return StrUtil.isEmpty(dateFormat) ?
			DateUtil.date(jsonReader.nextLong()) :
			DateUtil.parse(jsonReader.nextString(), dateFormat);
	}
}
