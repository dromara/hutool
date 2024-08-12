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

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.util.BooleanUtil;

/**
 * 布尔转换器
 *
 * <p>
 * 对象转为boolean，规则如下：
 * </p>
 * <pre>
 *     1、数字0为false，其它数字为true
 *     2、转换为字符串，形如"true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", "對", "√"为true，其它字符串为false.
 * </pre>
 *
 * @author Looly
 */
public class BooleanConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected Boolean convertInternal(final Class<?> targetClass, final Object value) {
		if (value instanceof Number) {
			// 0为false，其它数字为true
			return 0 != ((Number) value).doubleValue();
		}
		//Object不可能出现Primitive类型，故忽略
		return BooleanUtil.toBoolean(convertToStr(value));
	}

}
