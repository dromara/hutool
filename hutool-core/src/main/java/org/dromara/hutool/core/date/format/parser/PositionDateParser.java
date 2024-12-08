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

package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.format.DateBasic;
import org.dromara.hutool.core.lang.Assert;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * 带有{@link ParsePosition}的日期解析接口，用于解析日期字符串为 {@link Date} 对象<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @author Looly
 * @since 6.0.0
 */
public interface PositionDateParser extends DateParser, DateBasic {

	/**
	 * 根据给定格式更新{@link Calendar}<br>
	 * 解析成功后，{@link ParsePosition#getIndex()}更新成转换到的位置<br>
	 * 失败则{@link ParsePosition#getErrorIndex()}更新到解析失败的位置
	 *
	 * @param source   被转换的日期字符串
	 * @param pos      定义开始转换的位置，转换结束后更新转换到的位置，{@code null}表示忽略，从第一个字符开始转换
	 * @param calendar 解析并更新的{@link Calendar}
	 * @return 解析成功返回 {@code true}，否则返回{@code false}
	 */
	boolean parse(CharSequence source, ParsePosition pos, Calendar calendar);

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象<br>
	 * 等价于 {@link java.text.DateFormat#parse(String, ParsePosition)}
	 *
	 * @param source 日期字符串
	 * @param pos    {@link ParsePosition}
	 * @return {@link Date}
	 */
	default Date parse(final CharSequence source, final ParsePosition pos){
		return parseCalendar(source, pos, DateUtil.isGlobalLenient()).getTime();
	}

	/**
	 * 将日期字符串解析并转换为 {@link Calendar} 对象<br>
	 *
	 * @param source   日期字符串
	 * @param pos      {@link ParsePosition}
	 * @param lenient  是否宽容模式
	 * @return {@link Calendar}
	 */
	default Calendar parseCalendar(final CharSequence source, final ParsePosition pos, final boolean lenient){
		Assert.notBlank(source, "Date str must be not blank!");

		// timing tests indicate getting new instance is 19% faster than cloning
		final Calendar calendar = Calendar.getInstance(getTimeZone(), getLocale());
		calendar.clear();
		calendar.setLenient(lenient);

		if (parse(source.toString(), pos, calendar)) {
			return calendar;
		}

		throw new DateException("Parse [{}] with format [{}] error, at: {}",
			source, getPattern(), pos.getErrorIndex());
	}
}
