/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
