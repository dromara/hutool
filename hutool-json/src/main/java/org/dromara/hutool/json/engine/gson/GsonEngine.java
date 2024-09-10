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

package org.dromara.hutool.json.engine.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.engine.AbstractJSONEngine;
import org.dromara.hutool.json.engine.JSONEngineConfig;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * Gson引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class GsonEngine extends AbstractJSONEngine implements Wrapper<Gson> {

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
	public Gson getRaw() {
		initEngine();
		return this.gson;
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
		registerDate(builder, dateFormat);

		if(!config.isIgnoreNullValue()){
			builder.serializeNulls();
		}

		this.gson = builder.create();
	}

	/**
	 * 注册日期相关序列化描述<br>
	 * 参考：https://stackoverflow.com/questions/41979086/how-to-serialize-date-to-long-using-gson
	 *
	 * @param builder Gson构造器
	 * @param dateFormat 日期格式
	 */
	private void registerDate(final GsonBuilder builder, final String dateFormat){
		// java date
		builder.registerTypeHierarchyAdapter(Date.class, new DateSerDesc(dateFormat));
		builder.registerTypeHierarchyAdapter(TimeZone.class, TimeZoneSerDesc.INSTANCE);

		// java.time
		builder.registerTypeAdapter(LocalDateTime.class, new TemporalSerDesc(LocalDateTime.class, dateFormat));
		builder.registerTypeAdapter(LocalDate.class, new TemporalSerDesc(LocalDate.class, dateFormat));
		builder.registerTypeAdapter(LocalTime.class, new TemporalSerDesc(LocalTime.class, dateFormat));
	}
}
