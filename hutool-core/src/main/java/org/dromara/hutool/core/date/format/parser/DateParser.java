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
