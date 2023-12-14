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

package org.dromara.hutool.core.date.format.parser;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * 带有{@link ParsePosition}的日期解析接口，用于解析日期字符串为 {@link Date} 对象<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @since 2.16.2
 */
public interface PositionDateParser extends DateParser {

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象<br>
	 * 等价于 {@link java.text.DateFormat#parse(String, ParsePosition)}
	 *
	 * @param source 日期字符串
	 * @param pos    {@link ParsePosition}
	 * @return {@link Date}
	 */
	Date parse(String source, ParsePosition pos);

	/**
	 * 根据给定格式更新{@link Calendar}
	 * Upon success, the ParsePosition index is updated to indicate how much of the source text was consumed.
	 * Not all source text needs to be consumed.
	 * Upon parse failure, ParsePosition error index is updated to the offset of the source text which does not match the supplied format.
	 *
	 * @param source   被转换的日期字符串
	 * @param pos      定义开始转换的位置，转换结束后更新转换到的位置
	 * @param calendar The calendar into which to set parsed fields.
	 * @return true, if source has been parsed (pos parsePosition is updated); otherwise false (and pos errorIndex is updated)
	 * @throws IllegalArgumentException when Calendar has been set to be not lenient, and a parsed field is out of range.
	 */
	boolean parse(String source, ParsePosition pos, Calendar calendar);
}
