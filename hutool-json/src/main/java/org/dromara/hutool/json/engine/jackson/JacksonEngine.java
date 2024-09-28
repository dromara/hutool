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

package org.dromara.hutool.json.engine.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.engine.AbstractJSONEngine;
import org.dromara.hutool.json.engine.JSONEngineConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Jackson引擎
 *
 * @author Looly
 */
public class JacksonEngine extends AbstractJSONEngine implements Wrapper<ObjectMapper> {

	private ObjectMapper mapper;

	/**
	 * 构造
	 */
	public JacksonEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(ObjectMapper.class);
	}

	/**
	 * 获取Jackson的{@link ObjectMapper}对象
	 *
	 * @return {@link ObjectMapper}对象
	 */
	@Override
	public ObjectMapper getRaw() {
		initEngine();
		return mapper;
	}

	@Override
	public void serialize(final Object bean, final OutputStream out) {
		initEngine();
		try {
			mapper.writeValue(out, bean);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public String toJsonString(final Object bean) {
		initEngine();
		try {
			return mapper.writeValueAsString(bean);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		try {
			if (type instanceof Class) {
				return mapper.readValue(reader, (Class<T>) type);
			} else if (type instanceof TypeReference) {
				return mapper.readValue(reader, (TypeReference<T>) type);
			} else if (type instanceof JavaType) {
				return mapper.readValue(reader, (JavaType) type);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		throw new JSONException("Unsupported type: {}", type.getClass());
	}

	@Override
	protected void reset() {
		this.mapper = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.mapper) {
			return;
		}

		final ObjectMapper mapper = new ObjectMapper();

		// 默认配置
		mapper.enable(
			// 允许出现单引号
			JsonParser.Feature.ALLOW_SINGLE_QUOTES,
			// 允许没有引号的字段名(非标准)
			JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES
		);
		mapper.disable(
			// 空Bean默认转为{}
			SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// 支持Java8+日期格式
		registerModule(mapper, "com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");

		// 自定义配置
		final JSONEngineConfig config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		if(config.isPrettyPrint()){
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		final String dateFormat = config.getDateFormat();
		// 用于处理java.time库中对象的序列化和反序列化
		mapper.registerModule(new TemporalModule(dateFormat));
		if(StrUtil.isNotEmpty(dateFormat)){
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.setDateFormat(DateUtil.newSimpleFormat(dateFormat));
		}
		if(config.isIgnoreNullValue()){
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}

		this.mapper = mapper;
	}

	/**
	 * 注册模块
	 *
	 * @param mapper      Jackson的{@link ObjectMapper}对象
	 * @param moduleClass 模块类名
	 */
	@SuppressWarnings("SameParameterValue")
	private void registerModule(final ObjectMapper mapper, final String moduleClass) {
		final Class<?> aClass;
		try {
			aClass = Class.forName(moduleClass);
		} catch (final ClassNotFoundException ignore) {
			//用户未引入，则跳过不加载模块
			return;
		}
		mapper.registerModule((Module) ConstructorUtil.newInstance(aClass));
	}
}
