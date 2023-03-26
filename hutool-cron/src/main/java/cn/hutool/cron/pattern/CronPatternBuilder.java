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

package cn.hutool.cron.pattern;

import cn.hutool.core.lang.builder.Builder;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 定时任务表达式构建器
 *
 * @author looly
 * @since 5.8.0
 */
public class CronPatternBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	final String[] parts = new String[7];

	/**
	 * 创建构建器
	 * @return CronPatternBuilder
	 */
	public static CronPatternBuilder of() {
		return new CronPatternBuilder();
	}

	/**
	 * 设置值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param values 时间值列表
	 * @return this
	 */
	public CronPatternBuilder setValues(final Part part, final int... values) {
		for (final int value : values) {
			part.checkValue(value);
		}
		return set(part, ArrayUtil.join(values, ","));
	}

	/**
	 * 设置区间
	 *
	 * @param part  部分，如秒、分、时等
	 * @param begin 起始值
	 * @param end   结束值
	 * @return this
	 */
	public CronPatternBuilder setRange(final Part part, final int begin, final int end) {
		Assert.notNull(part );
		part.checkValue(begin);
		part.checkValue(end);
		return set(part, StrUtil.format("{}-{}", begin, end));
	}

	/**
	 * 设置对应部分的定时任务值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public CronPatternBuilder set(final Part part, final String value) {
		parts[part.ordinal()] = value;
		return this;
	}

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
