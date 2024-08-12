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
