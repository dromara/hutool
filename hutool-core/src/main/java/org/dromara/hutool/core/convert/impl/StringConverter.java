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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.impl.stringer.BlobStringer;
import org.dromara.hutool.core.convert.impl.stringer.ClobStringer;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.xml.XmlUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

/**
 * 字符串转换器，提供各种对象转换为字符串的逻辑封装
 *
 * @author Looly
 */
public class StringConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	private Map<Class<?>, Function<Object, String>> stringer;

	/**
	 * 加入自定义对象类型的toString规则
	 *
	 * @param clazz 类型
	 * @param stringFunction 序列化函数
	 * @return this
	 */
	public StringConverter putStringer(final Class<?> clazz, final Function<Object, String> stringFunction){
		if(null == stringer){
			stringer = new HashMap<>();
		}
		stringer.put(clazz, stringFunction);
		return this;
	}

	@Override
	protected String convertInternal(final Class<?> targetClass, final Object value) {

		// 自定义toString
		if(MapUtil.isNotEmpty(stringer)){
			final Function<Object, String> stringFunction = stringer.get(targetClass);
			if(null != stringFunction){
				return stringFunction.apply(value);
			}
		}

		if (value instanceof TimeZone) {
			return ((TimeZone) value).getID();
		} else if (value instanceof org.w3c.dom.Node) {
			return XmlUtil.toStr((org.w3c.dom.Node) value);
		} else if (value instanceof java.sql.Clob) {
			return ClobStringer.INSTANCE.apply(value);
		} else if (value instanceof java.sql.Blob) {
			return BlobStringer.INSTANCE.apply(value);
		} else if (value instanceof Type) {
			return ((Type) value).getTypeName();
		}

		// 其它情况
		return convertToStr(value);
	}
}
