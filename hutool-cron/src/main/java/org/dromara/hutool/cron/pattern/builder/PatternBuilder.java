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

package org.dromara.hutool.cron.pattern.builder;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.text.StrJoiner;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.cron.pattern.Part;

/**
 * 定时任务表达式构建器
 *
 * @author looly
 * @since 5.8.0
 */
public class PatternBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	final String[] parts = new String[7];

	/**
	 * 创建构建器
	 * @return CronPatternBuilder
	 */
	public static PatternBuilder of() {
		return new PatternBuilder();
	}

	// region ----- set
	/**
	 * 设置值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param values 时间值列表
	 * @return this
	 */
	public PatternBuilder setValues(final Part part, final int... values) {
		for (final int value : values) {
			part.checkValue(value);
		}
		return set(part, new PartBuilder.And(values));
	}

	/**
	 * 设置区间
	 *
	 * @param part  部分，如秒、分、时等
	 * @param begin 起始值
	 * @param end   结束值
	 * @return this
	 */
	public PatternBuilder setRange(final Part part, final int begin, final int end) {
		Assert.notNull(part);
		part.checkValue(begin);
		part.checkValue(end);
		return set(part, new PartBuilder.Range(begin, end));
	}

	/**
	 * 设置年对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"2024,2025"、"2015-2025"等
	 * @return this
	 */
	public PatternBuilder setYear(final PartBuilder value) {
		return set(Part.YEAR, value);
	}

	/**
	 * 设置周中的天对应部分的定时任务值
	 *
	 * @param value 表达式值，如"Sun"、"7"等
	 * @return this
	 */
	public PatternBuilder setDayOfWeek(final PartBuilder value) {
		return set(Part.DAY_OF_WEEK, value);
	}

	/**
	 * 设置月份对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder setMonth(final PartBuilder value) {
		return set(Part.MONTH, value);
	}

	/**
	 * 设置日对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder setDayOfMonth(final PartBuilder value) {
		return set(Part.DAY_OF_MONTH, value);
	}

	/**
	 * 设置时对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder setHour(final PartBuilder value) {
		return set(Part.HOUR, value);
	}

	/**
	 * 设置分对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder setMinute(final PartBuilder value) {
		return set(Part.MINUTE, value);
	}

	/**
	 * 设置秒对应部分的定时任务值
	 *
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder setSecond(final PartBuilder value) {
		return set(Part.SECOND, value);
	}

	/**
	 * 设置对应部分的定时任务值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder set(final Part part, final PartBuilder value) {
		return set(part, ObjUtil.apply(value, PartBuilder::build));
	}

	/**
	 * 设置对应部分的定时任务值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public PatternBuilder set(final Part part, final String value) {
		parts[part.ordinal()] = value;
		return this;
	}
	// endregion

	@Override
	public String build() {
		for (int i = Part.MINUTE.ordinal(); i < Part.YEAR.ordinal(); i++) {
			// 从分到周，用户未设置使用默认值
			// 秒和年如果不设置，忽略之
			if(StrUtil.isBlank(parts[i])){
				parts[i] = "*";
			}
		}

		return StrJoiner.of(StrUtil.SPACE)
				.setNullMode(StrJoiner.NullMode.IGNORE)
				.append(this.parts)
				.toString();
	}
}
