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

import org.dromara.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;

/**
 * 带有匹配的转换器<br>
 * 判断目标对象是否满足条件，满足则转换，否则跳过<br>
 * 实现此接口同样可以不判断断言而直接转换
 *
 * @author Looly
 * @since 6.0.0
 */
public interface MatcherConverter extends Converter {

	/**
	 * 判断需要转换的对象是否匹配当前转换器，满足则转换，否则跳过
	 *
	 * @param targetType 转换的目标类型，不能为{@code null}
	 * @param rawType     目标原始类型，当targetType为Class时，和此参数一致，不能为{@code null}
	 * @param value      需要转换的值
	 * @return 是否匹配
	 */
	boolean match(Type targetType, Class<?> rawType, Object value);

	/**
	 * 判断需要转换的对象是否匹配当前转换器，满足则转换，否则跳过
	 *
	 * @param targetType 转换的目标类型
	 * @param value      需要转换的值
	 * @return 是否匹配
	 */
	default boolean match(final Type targetType, final Object value) {
		return match(targetType, TypeUtil.getClass(targetType), value);
	}
}
