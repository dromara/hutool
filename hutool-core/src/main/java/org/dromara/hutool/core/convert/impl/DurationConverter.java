/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;

import java.time.Duration;
import java.time.temporal.TemporalAmount;

/**
 *
 * {@link Duration}对象转换器
 *
 * @author Looly
 * @since 5.0.0
 */
public class DurationConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected Duration convertInternal(final Class<?> targetClass, final Object value) {
		if(value instanceof TemporalAmount){
			return Duration.from((TemporalAmount) value);
		} else if(value instanceof Long){
			return Duration.ofMillis((Long) value);
		} else {
			return Duration.parse(convertToStr(value));
		}
	}

}
