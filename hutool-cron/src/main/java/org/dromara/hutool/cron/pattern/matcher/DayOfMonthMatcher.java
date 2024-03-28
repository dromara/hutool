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

package org.dromara.hutool.cron.pattern.matcher;

import org.dromara.hutool.core.date.Month;

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
			|| matchLastDay(value, getLastDay(month, isLeapYear)));
	}

	/**
	 * 获取指定值之后的匹配值，也可以是指定值本身<br>
	 * 如果表达式中存在最后一天（如使用"L"），则：
	 * <ul>
	 *     <li>4月、6月、9月、11月最多匹配到30日</li>
	 *     <li>4月闰年匹配到29日，非闰年28日</li>
	 * </ul>
	 *
	 * @param value 指定的值
	 * @param month 月份，从1开始
	 * @param isLeapYear 是否为闰年
	 * @return 匹配到的值或之后的值
	 */
	public int nextAfter(int value, final int month, final boolean isLeapYear) {
		final int minValue = this.minValue;
		if (value > minValue) {
			final boolean[] bValues = this.bValues;
			final int lastDay = getLastDay(month, isLeapYear);
			while (value < lastDay) {
				if (bValues[value]) {
					return value;
				}
				value++;
			}

			// value == lastDay
			if(test(31)){
				// 匹配当月最后一天
				return value;
			}
		}

		// 两种情况返回最小值
		// 一是给定值小于最小值，那下一个匹配值就是最小值
		// 二是给定值大于最大值，那下一个匹配值也是下一轮的最小值
		return minValue;
	}

	/**
	 * 是否匹配本月最后一天，规则如下：
	 * <pre>
	 * 1、闰年2月匹配是否为29
	 * 2、其它月份是否匹配最后一天的日期（可能为30或者31）
	 * 3、表达式包含最后一天（使用31表示）
	 * </pre>
	 *
	 * @param value      被检查的值
	 * @param lastDay    月份的最后一天
	 * @return 是否为本月最后一天
	 */
	private boolean matchLastDay(final int value, final int lastDay) {
		return value > 27 && test(31) && value == lastDay;
	}

	/**
	 * 获取最后一天
	 *
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 最后一天
	 */
	private static int getLastDay(final int month, final boolean isLeapYear) {
		return Month.getLastDay(month - 1, isLeapYear);
	}
}
