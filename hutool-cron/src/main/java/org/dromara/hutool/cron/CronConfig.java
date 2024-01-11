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

package org.dromara.hutool.cron;

import java.util.TimeZone;

/**
 * 定时任务配置类
 *
 * @author looly
 * @since 5.4.7
 */
public class CronConfig {

	/**
	 * 时区
	 */
	protected TimeZone timezone = TimeZone.getDefault();
	/**
	 * 是否支持秒匹配
	 */
	protected boolean matchSecond;

	/**
	 * 构造
	 */
	public CronConfig(){
	}

	/**
	 * 设置时区
	 *
	 * @param timezone 时区
	 * @return this
	 */
	public CronConfig setTimeZone(final TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 *
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return this.timezone;
	}

	/**
	 * 是否支持秒匹配
	 *
	 * @return {@code true}使用，{@code false}不使用
	 */
	public boolean isMatchSecond() {
		return this.matchSecond;
	}

	/**
	 * 设置是否支持秒匹配，默认不使用
	 *
	 * @param isMatchSecond {@code true}支持，{@code false}不支持
	 * @return this
	 */
	public CronConfig setMatchSecond(final boolean isMatchSecond) {
		this.matchSecond = isMatchSecond;
		return this;
	}
}
