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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;
import org.dromara.hutool.json.xml.JSONXMLParser;
import org.dromara.hutool.json.xml.ParseConfig;

import java.lang.reflect.Type;

/**
 * CharSequence类型适配器，用于处理未匹配的JSON类型。
 *
 * @author looly
 * @since 6.0.0
 */
public class CharSequenceTypeAdapter implements MatcherJSONSerializer<CharSequence>, MatcherJSONDeserializer<CharSequence> {

	/**
	 * 单例
	 */
	public static final CharSequenceTypeAdapter INSTANCE = new CharSequenceTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof CharSequence;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return CharSequence.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(final CharSequence bean, final JSONContext context) {
		final String jsonStr = StrUtil.trim(bean);
		if (StrUtil.startWith(jsonStr, '<')) {
			// 可能为XML
			final JSONObject jsonObject = context.getOrCreateObj();
			JSONXMLParser.of(ParseConfig.of(), null).parseJSONObject(jsonStr, jsonObject);
			return jsonObject;
		}

		return context.getFactory().ofParser(new JSONTokener(jsonStr)).parse();
	}

	@Override
	public CharSequence deserialize(final JSON json, final Type deserializeType) {
		if(json instanceof JSONPrimitive){
			return ((JSONPrimitive) json).getValue().toString();
		}
		return json.toString();
	}
}
