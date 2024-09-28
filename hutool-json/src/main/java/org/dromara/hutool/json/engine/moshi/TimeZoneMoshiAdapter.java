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
import com.squareup.moshi.Moshi;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.TimeZone;

/**
 * TimeZone类型适配器
 *
 * @author Looly
 */
public class TimeZoneMoshiAdapter extends JsonAdapter<TimeZone> {

	/**
	 * 创建工厂
	 */
	public static final Factory FACTORY = new Factory() {
		@Override
		public JsonAdapter<?> create(final Type type, final Set<? extends Annotation> set, final Moshi moshi) {
			if(TimeZone.class.isAssignableFrom(TypeUtil.getClass(type))){
				return INSTANCE;
			}
			return null;
		}
	};

	/**
	 * 单例
	 */
	public static final TimeZoneMoshiAdapter INSTANCE = new TimeZoneMoshiAdapter();

	@Override
	public void toJson(final JsonWriter jsonWriter, final TimeZone timeZone) throws IOException {
		if(null == timeZone){
			jsonWriter.nullValue();
			return;
		}
		jsonWriter.value(timeZone.getID());
	}

	@Override
	public TimeZone fromJson(final JsonReader jsonReader) throws IOException {
		return TimeZone.getTimeZone(jsonReader.nextString());
	}
}
