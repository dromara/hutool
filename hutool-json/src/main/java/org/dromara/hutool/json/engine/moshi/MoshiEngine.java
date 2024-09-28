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
import com.squareup.moshi.Moshi;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.dromara.hutool.core.io.stream.ReaderInputStream;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.engine.AbstractJSONEngine;
import org.dromara.hutool.json.engine.JSONEngineConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Moshi引擎实现
 *
 * @author looly
 * @since 6.0.0
 */
public class MoshiEngine extends AbstractJSONEngine implements Wrapper<Moshi> {

	private Moshi moshi;

	/**
	 * 构造
	 */
	public MoshiEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Moshi.class);
	}

	@Override
	public Moshi getRaw() {
		initEngine();
		return this.moshi;
	}

	@Override
	public void serialize(final Object bean, final OutputStream out) {
		final BufferedSink sink = Okio.buffer(Okio.sink(out));
		try {
			getAdapter(this.moshi, bean.getClass()).toJson(sink, bean);
		} catch (final IOException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public String toJsonString(final Object bean) {
		final JsonAdapter<Object> adapter = getAdapter(this.moshi, bean.getClass());
		return adapter.toJson(bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		final BufferedSource source = Okio.buffer(Okio.source(new ReaderInputStream(reader, CharsetUtil.UTF_8)));
		final JsonAdapter<Object> adapter = this.moshi.adapter((Type) type);
		try {
			return (T) adapter.fromJson(source);
		} catch (final IOException e) {
			throw new JSONException(e);
		}
	}

	@Override
	protected void reset() {
		this.moshi = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.moshi) {
			return;
		}

		// 自定义配置
		final JSONEngineConfig config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		final Moshi.Builder builder = new Moshi.Builder();

		// 注册日期相关序列化描述
		final String dateFormat = config.getDateFormat();
		registerDate(builder, dateFormat);

		this.moshi = builder.build();
	}

	/**
	 * 获取并配置{@link JsonAdapter}
	 *
	 * @param moshi {@link Moshi}
	 * @param type  Bean类型
	 * @return this
	 */
	private JsonAdapter<Object> getAdapter(final Moshi moshi, final Type type) {
		initEngine();
		JsonAdapter<Object> adapter = this.moshi.adapter(type);
		if (ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false)) {
			adapter = adapter.indent("  ");
		}
		if (!ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isIgnoreNullValue, true)) {
			adapter = adapter.serializeNulls();
		}
		return adapter;
	}

	/**
	 * 注册日期相关序列化描述
	 *
	 * @param builder    Gson构造器
	 * @param dateFormat 日期格式
	 */
	private void registerDate(final Moshi.Builder builder, final String dateFormat) {
		builder.add(DateMoshiAdapter.createFactory(dateFormat));
		builder.add(LocalDateTime.class, new TemporalMoshiAdapter(LocalDateTime.class, dateFormat));
		builder.add(LocalDate.class, new TemporalMoshiAdapter(LocalDate.class, dateFormat));
		builder.add(LocalTime.class, new TemporalMoshiAdapter(LocalTime.class, dateFormat));
		builder.add(TimeZoneMoshiAdapter.FACTORY);
	}
}
