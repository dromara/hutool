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

import java.lang.reflect.Type;
import java.util.TimeZone;

/**
 * 时区序列化描述
 *
 * @author Looly
 * @since 6.0.0
 */
public class TimeZoneGsonTypeAdapter implements GsonTypeAdapter<TimeZone> {

	/**
	 * 默认时区格式化描述
	 */
	public static final TimeZoneGsonTypeAdapter INSTANCE = new TimeZoneGsonTypeAdapter();

	@Override
	public JsonElement serialize(final TimeZone src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.getID());
	}

	@Override
	public TimeZone deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return TimeZone.getTimeZone(json.getAsString());
	}
}
