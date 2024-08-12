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

import org.dromara.hutool.core.date.format.DateBasic;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * 带有{@link ParsePosition}的日期解析接口，用于解析日期字符串为 {@link Date} 对象<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @since 2.16.2
 */
public interface PositionDateParser extends DateParser, DateBasic {

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象<br>
	 * 等价于 {@link java.text.DateFormat#parse(String, ParsePosition)}
	 *
	 * @param source 日期字符串
	 * @param pos    {@link ParsePosition}
	 * @return {@link Date}
	 */
	Date parse(CharSequence source, ParsePosition pos);

	/**
	 * 根据给定格式更新{@link Calendar}<br>
	 * 解析成功后，{@link ParsePosition#getIndex()}更新成转换到的位置<br>
	 * 失败则{@link ParsePosition#getErrorIndex()}更新到解析失败的位置
	 *
	 * @param source   被转换的日期字符串
	 * @param pos      定义开始转换的位置，转换结束后更新转换到的位置
	 * @param calendar 解析并更新的{@link Calendar}
	 * @return 解析成功返回 {@code true}，否则返回{@code false}
	 */
	boolean parse(CharSequence source, ParsePosition pos, Calendar calendar);
}
