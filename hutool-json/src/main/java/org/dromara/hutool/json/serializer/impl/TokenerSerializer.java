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

import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONFactory;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.io.InputStream;
import java.io.Reader;

/**
 * JSONTokener及其读取流的JSON序列化器实现
 *
 * @author looly
 * @since 6.0.0
 */
public class TokenerSerializer implements MatcherJSONSerializer<Object> {

	/**
	 * 单例
	 */
	public static final TokenerSerializer INSTANCE = new TokenerSerializer();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Reader
			|| bean instanceof InputStream;
	}

	@Override
	public JSON serialize(final Object bean, final JSONContext context) {
		// 读取JSON流
		if (bean instanceof JSONTokener) {
			return mapFromTokener((JSONTokener) bean, context.getFactory());
		} else if (bean instanceof JSONParser) {
			return ((JSONParser) bean).parse();
		} else if (bean instanceof Reader) {
			return mapFromTokener(new JSONTokener((Reader) bean, context.config().isIgnoreZeroWithChar()), context.getFactory());
		} else if (bean instanceof InputStream) {
			return mapFromTokener(new JSONTokener((InputStream) bean, context.config().isIgnoreZeroWithChar()), context.getFactory());
		}

		throw new IllegalArgumentException("Unsupported source: " + bean);
	}

	/**
	 * 从{@link JSONTokener} 中读取JSON字符串，并转换为JSON
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSON
	 */
	private JSON mapFromTokener(final JSONTokener tokener, final JSONFactory factory) {
		return factory.ofParser(tokener).parse();
	}
}
