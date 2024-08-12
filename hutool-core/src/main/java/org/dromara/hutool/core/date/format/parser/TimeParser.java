/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Serializable;

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
public class TimeParser implements PredicateDateParser, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final TimeParser INSTANCE = new TimeParser();

	@Override
	public boolean test(final CharSequence dateStr) {
		return ReUtil.isMatch(PatternPool.TIME, dateStr);
	}

	@Override
	public DateTime parse(CharSequence source) {
		// issue#I9C2D4 处理时分秒
		//15时45分59秒 修正为 15:45:59
		source = StrUtil.replaceChars(source, "时分秒", ":");

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
