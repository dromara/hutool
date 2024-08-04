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

import org.dromara.hutool.core.date.DateException;

import java.util.Date;

/**
 * 日期解析接口，用于解析日期字符串为 {@link Date} 对象
 *
 * @author Looly
 */
@FunctionalInterface
public interface DateParser {

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象
	 *
	 * @param source 被解析的日期字符串
	 * @return {@link Date}对象
	 * @throws DateException 转换异常，被转换的字符串格式错误。
	 */
	Date parse(CharSequence source) throws DateException;
}
