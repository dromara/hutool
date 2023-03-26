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

package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.date.Month;

import java.util.List;

/**
 * 每月第几天匹配<br>
 * 考虑每月的天数不同，且存在闰年情况，日匹配单独使用
 *
 * @author Looly
 */
public class DayOfMonthMatcher extends BoolArrayMatcher {

	/**
	 * 构造
	 *
	 * @param intValueList 匹配的日值
	 */
	public DayOfMonthMatcher(final List<Integer> intValueList) {
		super(intValueList);
	}

	/**
	 * 给定的日期是否匹配当前匹配器
	 *
	 * @param value      被检查的值，此处为日
	 * @param month      实际的月份，从1开始
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	public boolean match(final int value, final int month, final boolean isLeapYear) {
		return (super.test(value) // 在约定日范围内的某一天
				//匹配器中用户定义了最后一天（31表示最后一天）
				|| (value > 27 && test(31) && isLastDayOfMonth(value, month, isLeapYear)));
	}

	/**
	 * 是否为本月最后一天，规则如下：
	 * <pre>
	 * 1、闰年2月匹配是否为29
	 * 2、其它月份是否匹配最后一天的日期（可能为30或者31）
	 * </pre>
	 *
	 * @param value      被检查的值
	 * @param month      月份，从1开始
	 * @param isLeapYear 是否闰年
	 * @return 是否为本月最后一天
	 */
	private static boolean isLastDayOfMonth(final int value, final int month, final boolean isLeapYear) {
		return value == Month.getLastDay(month - 1, isLeapYear);
	}
}
