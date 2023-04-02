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

import org.dromara.hutool.date.DateException;
import org.dromara.hutool.date.DatePattern;
import org.dromara.hutool.date.DateTime;
import org.dromara.hutool.date.format.DefaultDateBasic;

/**
 * 纯数字的日期字符串解析，支持格式包括；
 * <ul>
 *   <li>yyyyMMddHHmmss</li>
 *   <li>yyyyMMddHHmmssSSS</li>
 *   <li>yyyyMMdd</li>
 *   <li>HHmmss</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class PureDateParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static PureDateParser INSTANCE = new PureDateParser();

	@Override
	public DateTime parse(final String source) throws DateException {
		final int length = source.length();
		// 纯数字形式
		if (length == DatePattern.PURE_DATETIME_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATETIME_FORMAT);
		} else if (length == DatePattern.PURE_DATETIME_MS_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATETIME_MS_FORMAT);
		} else if (length == DatePattern.PURE_DATE_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATE_FORMAT);
		} else if (length == DatePattern.PURE_TIME_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_TIME_FORMAT);
		}

		throw new DateException("No pure format fit for date String [{}] !", source);
	}

}
