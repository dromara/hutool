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

package org.dromara.hutool.core.date.format;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期基本信息获取接口
 *
 * @author Looly
 * @since 2.16.2
 */
public interface DateBasic {

	/**
	 * 获得日期格式化或者转换的格式
	 *
	 * @return {@link java.text.SimpleDateFormat}兼容的格式
	 */
	String getPattern();

	/**
	 * 获得时区
	 *
	 * @return {@link TimeZone}
	 */
	TimeZone getTimeZone();

	/**
	 * 获得 日期地理位置
	 *
	 * @return {@link Locale}
	 */
	Locale getLocale();
}
