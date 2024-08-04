/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
