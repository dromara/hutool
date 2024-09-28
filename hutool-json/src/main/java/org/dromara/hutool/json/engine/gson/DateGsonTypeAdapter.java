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
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * 日期序列化描述<br>
 * 参考：https://stackoverflow.com/questions/41979086/how-to-serialize-date-to-long-using-gson
 *
 * @author Looly
 * @since 6.0.0
 */
public class DateGsonTypeAdapter implements GsonTypeAdapter<Date> {

	/**
	 * 默认日期格式化描述，默认为null，表示使用时间戳
	 */
	public static final DateGsonTypeAdapter INSTANCE = new DateGsonTypeAdapter(null);

	private final String dateFormat;

	/**
	 * 构造
	 *
	 * @param dateFormat 日期格式
	 */
	public DateGsonTypeAdapter(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {
		return StrUtil.isEmpty(dateFormat) ?
			new JsonPrimitive(src.getTime()) :
			new JsonPrimitive(DateUtil.format(src, dateFormat));
	}

	@Override
	public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return StrUtil.isEmpty(dateFormat) ?
			DateUtil.date(json.getAsLong()) :
			DateUtil.parse(json.getAsString(), dateFormat);
	}
}
