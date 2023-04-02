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

package org.dromara.hutool.convert.impl;

import org.dromara.hutool.convert.AbstractConverter;
import org.dromara.hutool.date.ZoneUtil;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * {@link ZoneId}转换器
 *
 * @author Looly
 */
public class ZoneIdConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected ZoneId convertInternal(final Class<?> targetClass, final Object value) {
		if (value instanceof TimeZone) {
			return ZoneUtil.toZoneId((TimeZone) value);
		}
		return ZoneId.of(convertToStr(value));
	}

}
