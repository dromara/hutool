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

import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.format.DefaultDateBasic;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.util.regex.Pattern;

/**
 * 标准日期字符串解析，支持格式；
 * <pre>
 *     yyyy-MM-dd HH:mm:ss.SSSSSS
 *     yyyy-MM-dd HH:mm:ss.SSS
 *     yyyy-MM-dd HH:mm:ss
 *     yyyy-MM-dd HH:mm
 *     yyyy-MM-dd
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class NormalDateParser extends DefaultDateBasic implements PredicateDateParser {
	private static final long serialVersionUID = 1L;

	private static final Pattern patternNorm = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}(:\\d{1,2})?(.\\d{1,6})?)?");

	/**
	 * 单例
	 */
	public static NormalDateParser INSTANCE = new NormalDateParser();

	@Override
	public boolean test(final CharSequence dateStr) {
		return ReUtil.isMatch(patternNorm, dateStr);
	}

	@Override
	public DateTime parse(String source) throws DateException{
		final int colonCount = StrUtil.count(source, CharUtil.COLON);
		switch (colonCount) {
			case 0:
				// yyyy-MM-dd
				return new DateTime(source, DatePattern.NORM_DATE_FORMAT);
			case 1:
				// yyyy-MM-dd HH:mm
				return new DateTime(source, DatePattern.NORM_DATETIME_MINUTE_FORMAT);
			case 2:
				final int indexOfDot = StrUtil.indexOf(source, CharUtil.DOT);
				if (indexOfDot > 0) {
					final int length1 = source.length();
					// yyyy-MM-dd HH:mm:ss.SSS 或者 yyyy-MM-dd HH:mm:ss.SSSSSS
					if (length1 - indexOfDot > 4) {
						// 类似yyyy-MM-dd HH:mm:ss.SSSSSS，采取截断操作
						source = StrUtil.subPre(source, indexOfDot + 4);
					}
					return new DateTime(source, DatePattern.NORM_DATETIME_MS_FORMAT);
				}
				// yyyy-MM-dd HH:mm:ss
				return new DateTime(source, DatePattern.NORM_DATETIME_FORMAT);
		}

		throw new DateException("No format fit for date String [{}] !", source);
	}
}
