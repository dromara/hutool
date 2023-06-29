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

import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.lang.tuple.Tuple;

import java.lang.reflect.Type;

/**
 * {@link Tuple}转换器
 *
 * @author looly
 * @since 6.0.0
 */
public class TupleConverter implements Converter {

	/**
	 * 单例
	 */
	public static final TupleConverter INSTANCE = new TupleConverter();

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		final Object[] convert = (Object[]) ArrayConverter.INSTANCE.convert(Object[].class, value);
		return Tuple.of(convert);
	}
}
