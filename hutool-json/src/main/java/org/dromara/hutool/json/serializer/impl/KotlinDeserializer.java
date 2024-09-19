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
import org.dromara.hutool.core.reflect.kotlin.KClassUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONGetter;
import org.dromara.hutool.json.convert.JSONGetterValueProvider;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;

import java.lang.reflect.Type;

/**
 * Kotlin对象反序列化器<br>
 * issue#I5WDP0 对于Kotlin对象，由于参数可能非空限制，导致无法创建一个默认的对象再赋值
 *
 * @author looly
 * @since 6.0.0
 */
public class KotlinDeserializer implements MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final MatcherJSONDeserializer<?> INSTANCE = new KotlinDeserializer();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		final Class<?> rawType = TypeUtil.getClass(deserializeType);
		return json instanceof JSONGetter
			&& KClassUtil.isKotlinClass(rawType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		final Class<?> rawType = TypeUtil.getClass(deserializeType);
		return KClassUtil.newInstance(rawType, new JSONGetterValueProvider<>((JSONGetter<String>) json));
	}
}
