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

package org.dromara.hutool.date.format.parser;

import org.dromara.hutool.date.DatePattern;
import org.dromara.hutool.date.DateTime;
import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.date.format.DefaultDateBasic;
import org.dromara.hutool.text.StrUtil;

/**
 * 时间日期字符串，日期默认为当天，支持格式类似于；
 * <pre>
 *   HH:mm:ss
 *   HH:mm
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class TimeParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static TimeParser INSTANCE = new TimeParser();

	@Override
	public DateTime parse(String source) {
		source = StrUtil.format("{} {}", DateUtil.formatToday(), source);
		if (1 == StrUtil.count(source, ':')) {
			// 时间格式为 HH:mm
			return new DateTime(source, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
		} else {
			// 时间格式为 HH:mm:ss
			return new DateTime(source, DatePattern.NORM_DATETIME_FORMAT);
		}
	}
}
