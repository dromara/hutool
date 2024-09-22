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

import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.serializer.JSONDeserializer;

import java.lang.reflect.Type;

/**
 * 多元组Tuple反序列化器
 *
 * @author Looly
 * @since 6.0.0
 */
public class TupleDeserializer implements JSONDeserializer<Tuple> {

	/**
	 * 单例
	 */
	public static final TupleDeserializer INSTANCE = new TupleDeserializer();

	@Override
	public Tuple deserialize(final JSON json, final Type deserializeType) {
		return Tuple.of(json.toBean(Object[].class));
	}
}
