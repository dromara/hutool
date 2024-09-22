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

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.BeanToMapCopier;
import org.dromara.hutool.core.bean.copier.ValueProviderToBeanCopier;
import org.dromara.hutool.core.lang.copier.Copier;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.InternalJSONUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;

/**
 * Bean对象适配器，将Bean对象序列化为JSONObject，反序列化为Bean对象
 *
 * @author looly
 * @since 6.0.0
 */
public class BeanTypeAdapter implements MatcherJSONSerializer<Object>, MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final BeanTypeAdapter INSTANCE = new BeanTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		final JSON contextJson = ObjUtil.apply(context, JSONContext::getContextJson);
		return BeanUtil.isReadableBean(bean.getClass())
			&& (null == contextJson || contextJson instanceof JSONObject);
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return json instanceof JSONObject && BeanUtil.isWritableBean(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(final Object bean, final JSONContext context) {
		JSONObject contextJson = (JSONObject) ObjUtil.apply(context, JSONContext::getContextJson);
		if(null == contextJson){
			contextJson = new JSONObject(context.config());
		}

		final BeanToMapCopier copier = new BeanToMapCopier(
			bean,
			contextJson,
			JSONObject.class, InternalJSONUtil.toCopyOptions(context.config())
		);
		return (JSON) copier.copy();
	}

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		final Copier<Object> copier = new ValueProviderToBeanCopier<>(
			new JSONObjectValueProvider((JSONObject) json),
			ConstructorUtil.newInstanceIfPossible(TypeUtil.getClass(deserializeType)),
			deserializeType,
			InternalJSONUtil.toCopyOptions(json.config())
		);
		return copier.copy();
	}
}
