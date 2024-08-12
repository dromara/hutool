/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Jackson引擎
 *
 * @author Looly
 */
public class JacksonEngine extends AbstractJSONEngine {

	private ObjectMapper mapper;

	/**
	 * 构造
	 */
	public JacksonEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(ObjectMapper.class);
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		try {
			mapper.writeValue(writer, bean);
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
		if(null != this.mapper){
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

		// 自定义配置
		if(ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false)){
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}

		this.mapper = mapper;
	}
}
