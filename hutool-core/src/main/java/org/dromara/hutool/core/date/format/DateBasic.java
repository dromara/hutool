/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
