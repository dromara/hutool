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
import org.dromara.hutool.core.date.format.DefaultDateBasic;
import org.dromara.hutool.core.text.StrUtil;

/**
 * CST日期字符串（JDK的Date对象toString默认格式）解析，支持格式类似于；
 * <pre>
 *   Tue Jun 4 16:25:15 +0800 2019
 *   Thu May 16 17:57:18 GMT+08:00 2019
 *   Wed Aug 01 00:00:00 CST 2012
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class CSTDateParser extends DefaultDateBasic implements PredicateDateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * java.util.Date EEE MMM zzz 缩写数组
	 */
	private final static String[] wtb = { //
		"sun", "mon", "tue", "wed", "thu", "fri", "sat", // 星期
		"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", // 月份
		"gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"// 时间标准
	};

	/**
	 * 单例对象
	 */
	public static CSTDateParser INSTANCE = new CSTDateParser();

	@Override
	public boolean test(final CharSequence dateStr) {
		return StrUtil.containsAnyIgnoreCase(dateStr, wtb);
	}

	@Override
	public DateTime parse(final String source) {
		return new DateTime(source, DatePattern.JDK_DATETIME_FORMAT);
	}
}
