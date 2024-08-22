/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
	 * @param value      指定的值
	 * @param month      月份，从1开始
	 * @param isLeapYear 是否为闰年
	 * @return 匹配到的值或之后的值
	 */
	public int nextAfter(int value, final int month, final boolean isLeapYear) {
		final int minValue = getMinValue(month, isLeapYear);
		if (value > minValue) {
			final boolean[] bValues = this.bValues;
			while (value < bValues.length) {
				if (bValues[value]) {
					if(31 == value){
						// value == lastDay
						return getLastDay(month, isLeapYear);
					}
					return value;
				}
				value++;
			}
		}

		// 两种情况返回最小值
		// 一是给定值小于最小值，那下一个匹配值就是最小值
		// 二是给定值大于最大值，那下一个匹配值也是下一轮的最小值
		return minValue;
	}

	/**
	 * 获取匹配的最小值
	 *
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 匹配的最小值
	 */
	public int getMinValue(final int month, final boolean isLeapYear) {
		final int minValue = super.getMinValue();
		if (31 == minValue) {
			// 用户指定了 L 等表示最后一天
			return getLastDay(month, isLeapYear);
		}
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
	 * @param value   被检查的值
	 * @param lastDay 月份的最后一天
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
