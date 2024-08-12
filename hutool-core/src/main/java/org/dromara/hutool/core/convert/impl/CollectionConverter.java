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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 各种集合类转换器
 *
 * @author Looly
 * @since 3.0.8
 */
public class CollectionConverter implements Converter {

	/**
	 * 单例实体
	 */
	public static CollectionConverter INSTANCE = new CollectionConverter();

	@Override
	public Collection<?> convert(Type targetType, final Object value) {
		if (targetType instanceof TypeReference) {
			targetType = ((TypeReference<?>) targetType).getType();
		}

		return convert(targetType, TypeUtil.getTypeArgument(targetType), value);
	}

	/**
	 * 转换
	 *
	 * @param collectionType 集合类型
	 * @param elementType    集合中元素类型
	 * @param value          被转换的值
	 * @return 转换后的集合对象
	 */
	public Collection<?> convert(final Type collectionType, final Type elementType, final Object value) {
		// pr#2684，兼容EnumSet创建
		final Collection<?> collection = CollUtil.create(TypeUtil.getClass(collectionType), TypeUtil.getClass(elementType));
		return CollUtil.addAll(collection, value, elementType);
	}
}
