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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.util.ObjUtil;

import java.lang.reflect.Type;

/**
 * 类型转换接口函数，根据给定的值和目标类型，由用户自定义转换规则。
 *
 * @author looly
 * @since 6.0.0
 */
@FunctionalInterface
public interface Converter {

	/**
	 * 转换为指定类型<br>
	 * 如果类型无法确定，将读取默认值的类型做为目标类型
	 *
	 * @param targetType 目标Type，非泛型类使用
	 * @param value      原始值，如果对象实现了此接口，则value为this
	 * @return 转换后的值
	 * @throws ConvertException 转换无法正常完成或转换异常时抛出此异常
	 */
	Object convert(Type targetType, Object value) throws ConvertException;

	/**
	 * 转换值为指定类型，可选是否不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param <T>          目标类型
	 * @param targetType   目标类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	default <T> T convert(final Type targetType, final Object value, final T defaultValue) {
		return (T) ObjUtil.defaultIfNull(convert(targetType, value), defaultValue);
	}

	/**
	 * 返回原值的转换器，不做转换
	 * @return Converter
	 */
	static Converter identity(){
		return (targetType, value) -> value;
	}
}
