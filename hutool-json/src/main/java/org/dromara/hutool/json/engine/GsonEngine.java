/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.engine;

import com.google.gson.*;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Gson引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class GsonEngine extends AbstractJSONEngine {

	private Gson gson;

	/**
	 * 构造
	 */
	public GsonEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Gson.class);
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		gson.toJson(bean, writer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		if (type instanceof Class) {
			return gson.fromJson(reader, (Class<T>) type);
		} else if (type instanceof Type) {
			return gson.fromJson(reader, (Type) type);
		}

		throw new JSONException("Unsupported type: {}", type.getClass());
	}

	@Override
	protected void reset() {
		this.gson = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.gson) {
			return;
		}

		// 自定义配置
		final JSONEngineConfig config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		final GsonBuilder builder = new GsonBuilder();
		if (config.isPrettyPrint()) {
			builder.setPrettyPrinting();
		}
		final String dateFormat = config.getDateFormat();
		if (StrUtil.isNotEmpty(dateFormat)) {
			builder.setDateFormat(dateFormat);
			builder.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> TimeUtil.parse(json.getAsString(), dateFormat));
			builder.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (date, type, jsonSerializationContext) -> new JsonPrimitive(TimeUtil.format(date, dateFormat)));
		} else {
			// 无自定义格式，则默认输出时间戳
			// https://stackoverflow.com/questions/41979086/how-to-serialize-date-to-long-using-gson
			builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
			builder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()));
			builder.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> TimeUtil.of(json.getAsJsonPrimitive().getAsLong()));
			builder.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (date, type, jsonSerializationContext) -> new JsonPrimitive(TimeUtil.toEpochMilli(date)));
		}
		if(!config.isIgnoreNullValue()){
			builder.serializeNulls();
		}

		this.gson = builder.create();
	}
}
