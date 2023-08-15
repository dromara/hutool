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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.range.Range;

import java.util.Date;

/**
 * 日期范围
 *
 * @author looly
 * @since 4.1.0
 */
public class DateRange extends Range<DateTime> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造，包含开始和结束日期时间
	 *
	 * @param start 起始日期时间（包括）
	 * @param end 结束日期时间（包括）
	 * @param unit 步进单位
	 */
	public DateRange(final Date start, final Date end, final DateField unit) {
		this(start, end, unit, 1);
	}

	/**
	 * 构造，包含开始和结束日期时间
	 *
	 * @param start 起始日期时间（包括）
	 * @param end 结束日期时间（包括）
	 * @param unit 步进单位
	 * @param step 步进数
	 */
	public DateRange(final Date start, final Date end, final DateField unit, final int step) {
		this(start, end, unit, step, true, true);
	}

	/**
	 * 构造
	 *
	 * @param start 起始日期时间
	 * @param end 结束日期时间
	 * @param unit 步进单位
	 * @param step 步进数
	 * @param isIncludeStart 是否包含开始的时间
	 * @param isIncludeEnd 是否包含结束的时间
	 */
	public DateRange(final Date start, final Date end, final DateField unit, final int step, final boolean isIncludeStart, final boolean isIncludeEnd) {
		super(DateUtil.date(start), DateUtil.date(end), (current, end1, index) -> {
			final DateTime dt = DateUtil.date(start).offsetNew(unit, (index + 1) * step);
			if (dt.isAfter(end1)) {
				return null;
			}
			return dt;
		}, isIncludeStart, isIncludeEnd);
	}

}
