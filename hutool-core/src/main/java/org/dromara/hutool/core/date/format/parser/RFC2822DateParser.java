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

import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.format.FastDateFormat;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Serializable;
import java.util.Locale;

/**
 * RFC2822日期字符串（JDK的Date对象toString默认格式）及HTTP消息日期解析，支持格式类似于；
 * <pre>
 *   Tue Jun 4 16:25:15 +0800 2019
 *   Thu May 16 17:57:18 GMT+08:00 2019
 *   Wed Aug 01 00:00:00 CST 2012
 *   Thu, 28 Mar 2024 14:33:49 GMT
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class RFC2822DateParser implements PredicateDateParser, Serializable {
	private static final long serialVersionUID = 1L;

	private static final String KEYWORDS_LOCALE_CHINA = "星期";

	/**
	 * java.util.Date EEE MMM zzz 缩写数组
	 */
	private static final String[] wtb = { //
		"sun", "mon", "tue", "wed", "thu", "fri", "sat", // 星期
		"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", // 月份
		"gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"// 时间标准
	};

	/**
	 * 单例对象
	 */
	public static RFC2822DateParser INSTANCE = new RFC2822DateParser();

	@Override
	public boolean test(final CharSequence dateStr) {
		return StrUtil.containsAnyIgnoreCase(dateStr, wtb);
	}

	@Override
	public DateTime parse(final CharSequence source) {
		// issue#I9C2D4
		if (StrUtil.contains(source, ',')) {
			if (StrUtil.contains(source, KEYWORDS_LOCALE_CHINA)) {
				// 例如：星期四, 28 三月 2024 14:33:49 GMT
				return new DateTime(source, FastDateFormat.getInstance(DatePattern.HTTP_DATETIME_PATTERN, Locale.CHINA));
			}
			// 例如：Thu, 28 Mar 2024 14:33:49 GMT
			return new DateTime(source, DatePattern.HTTP_DATETIME_FORMAT);
		}

		if (StrUtil.contains(source, KEYWORDS_LOCALE_CHINA)) {
			// 例如：星期四, 28 三月 2024 14:33:49 GMT
			return new DateTime(source, FastDateFormat.getInstance(DatePattern.JDK_DATETIME_PATTERN, Locale.CHINA));
		}
		// 例如：Thu 28 Mar 2024 14:33:49 GMT
		return new DateTime(source, DatePattern.JDK_DATETIME_FORMAT);
	}
}
