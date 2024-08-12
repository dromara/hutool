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

package org.dromara.hutool.core.date.chinese;

/**
 * 农历标准化输出格式枚举
 *
 * @author eventiming
 */
public enum ChineseDateFormat {

	/**
	 * 干支纪年 数序纪月 数序纪日
	 */
	GSS("干支纪年 数序纪月 数序纪日"),
	/**
	 * 生肖纪年 数序纪月 数序纪日
	 */
	XSS("生肖纪年 数序纪月 数序纪日"),
	/**
	 * 干支生肖纪年 数序纪月（传统表示） 数序纪日
	 */
	GXSS("干支生肖纪年 数序纪月（传统表示） 数序纪日"),
	/**
	 * 干支纪年 数序纪月 干支纪日
	 */
	GSG("干支纪年 数序纪月 干支纪日"),
	/**
	 * 干支纪年 干支纪月 干支纪日
	 */
	GGG("干支纪年 干支纪月 干支纪日"),
	/**
	 * 农历年年首所在的公历年份 干支纪年 数序纪月 数序纪日
	 */
	MIX("农历年年首所在的公历年份 干支纪年 数序纪月 数序纪日");

	/**
	 * 农历标准化输出格式信息
	 */
	private final String info;

	/**
	 * 构造
	 *
	 * @param info 输出格式信息
	 */
	ChineseDateFormat(final String info) {
		this.info = info;
	}

	/**
	 * 获取农历日期输出格式相关描述
	 *
	 * @return 输出格式信息
	 */
	public String getName() {
		return this.info;
	}
}
