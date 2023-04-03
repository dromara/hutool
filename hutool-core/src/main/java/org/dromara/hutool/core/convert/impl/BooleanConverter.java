/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
