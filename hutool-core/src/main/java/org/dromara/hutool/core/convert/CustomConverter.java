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

import org.dromara.hutool.core.collection.set.ConcurrentHashSet;
import org.dromara.hutool.core.stream.StreamUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * 用户自定义转换器<br>
 * 通过自定义实现{@link MatcherConverter}，可以通过{@link MatcherConverter#match(Type, Class, Object)} 检查目标类型是否匹配<br>
 * 如果匹配，则直接转换，否则使用默认转换器转换。
 *
 * @author Looly
 * @since 6.0.0
 */
public class CustomConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final CustomConverter INSTANCE = new CustomConverter();
	}

	/**
	 * 获得单例的 CustomConverter
	 *
	 * @return CustomConverter
	 */
	public static CustomConverter getInstance() {
		return CustomConverter.SingletonHolder.INSTANCE;
	}

	/**
	 * 用户自定义类型转换器
	 */
	private volatile Set<MatcherConverter> converterSet;

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		final Converter customConverter = getCustomConverter(targetType, value);
		return null == customConverter ? null : customConverter.convert(targetType, value);
	}

	/**
	 * 获得匹配类型的自定义转换器
	 *
	 * @param type  类型
	 * @param value 被转换的值
	 * @return 转换器
	 */
	public Converter getCustomConverter(final Type type, final Object value) {
		return getConverterFromSet(this.converterSet, type, value);
	}

	/**
	 * 登记自定义转换器
	 *
	 * @param converter 转换器
	 * @return ConverterRegistry
	 */
	public CustomConverter add(final MatcherConverter converter) {
		if (null == this.converterSet) {
			synchronized (this) {
				if (null == this.converterSet) {
					this.converterSet = new ConcurrentHashSet<>();
				}
			}
		}
		this.converterSet.add(converter);
		return this;
	}

	/**
	 * 从指定集合中查找满足条件的转换器
	 *
	 * @param type 类型
	 * @return 转换器
	 */
	private static Converter getConverterFromSet(final Set<? extends MatcherConverter> converterSet, final Type type, final Object value) {
		return StreamUtil.of(converterSet).filter((predicate) -> predicate.match(type, value)).findFirst().orElse(null);
	}
}
