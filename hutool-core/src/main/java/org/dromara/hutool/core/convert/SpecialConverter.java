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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.convert.impl.*;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.stream.StreamUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 特殊类型转换器，如果不符合特殊类型，则返回{@code null}继续其它转换规则<br>
 * 对于特殊对象（如集合、Map、Enum、数组）等的转换器，实现转换<br>
 * 注意：此类中的转换器查找是通过遍历方式
 *
 * @author Looly
 * @since 6.0.0
 */
public class SpecialConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final SpecialConverter INSTANCE = new SpecialConverter();
	}

	/**
	 * 获得单例的 CustomConverter
	 *
	 * @return CustomConverter
	 */
	public static SpecialConverter getInstance() {
		return SpecialConverter.SingletonHolder.INSTANCE;
	}

	/**
	 * 类型转换器集合<br>
	 * 此集合初始化后不再加入新值，因此单例使用线程安全
	 */
	private final Set<MatcherConverter> converterSet;

	/**
	 * 构造
	 */
	private SpecialConverter() {
		final Set<MatcherConverter> converterSet = new LinkedHashSet<>(64);

		// 集合转换（含有泛型参数，不可以默认强转）
		converterSet.add(CollectionConverter.INSTANCE);
		// Map类型（含有泛型参数，不可以默认强转）
		converterSet.add(MapConverter.INSTANCE);
		// issue#I6SZYB Entry类（含有泛型参数，不可以默认强转）
		converterSet.add(EntryConverter.INSTANCE);
		// 默认强转
		converterSet.add(CastConverter.INSTANCE);
		// 日期、java.sql中的日期以及自定义日期统一处理
		converterSet.add(DateConverter.INSTANCE);
		// 原始类型转换
		converterSet.add(PrimitiveConverter.INSTANCE);
		// 数字类型转换
		converterSet.add(NumberConverter.INSTANCE);
		// 枚举转换
		converterSet.add(EnumConverter.INSTANCE);
		// 数组转换
		converterSet.add(ArrayConverter.INSTANCE);
		// Record
		converterSet.add(RecordConverter.INSTANCE);
		// Kotlin Bean
		converterSet.add(KBeanConverter.INSTANCE);
		// issue#I7FQ29 Class
		converterSet.add(ClassConverter.INSTANCE);
		// // 空值转空Bean
		converterSet.add(EmptyBeanConverter.INSTANCE);

		// 日期相关
		converterSet.add(TimeZoneConverter.INSTANCE);
		converterSet.add(ZoneIdConverter.INSTANCE);

		this.converterSet = converterSet;
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		return convert(targetType, TypeUtil.getClass(targetType), value);
	}

	/**
	 * 转换值
	 *
	 * @param targetType 目标类型
	 * @param rawType    目标原始类型（即目标的Class）
	 * @param value      被转换的值
	 * @return 转换后的值，如果无转换器，返回{@code null}
	 * @throws ConvertException 转换异常，即找到了对应的转换器，但是转换失败
	 */
	public Object convert(final Type targetType, final Class<?> rawType, final Object value) throws ConvertException {
		final Converter converter = getConverter(targetType, rawType, value);
		return null == converter ? null : converter.convert(targetType, value);
	}

	/**
	 * 获得匹配的转换器
	 *
	 * @param type    类型
	 * @param rawType 目标类型的Class
	 * @param value   被转换的值
	 * @return 转换器
	 */
	public Converter getConverter(final Type type, final Class<?> rawType, final Object value) {
		return getConverterFromSet(this.converterSet, type, rawType, value);
	}

	/**
	 * 从指定集合中查找满足条件的转换器
	 *
	 * @param type 类型
	 * @return 转换器
	 */
	private static Converter getConverterFromSet(final Set<? extends MatcherConverter> converterSet, final Type type, final Class<?> rawType, final Object value) {
		return StreamUtil.of(converterSet).filter((predicate) -> predicate.match(type, rawType, value)).findFirst().orElse(null);
	}
}
