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

package org.dromara.hutool.core.date;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * {@link ZoneId}和{@link TimeZone}相关封装
 *
 * @author looly
 * @since 5.7.15
 */
public class ZoneUtil {

	/**
	 * UTC 的 ZoneID
	 */
	public static final TimeZone ZONE_UTC = TimeZone.getTimeZone("UTC");
	/**
	 * UTC 的 TimeZone
	 */
	public static final ZoneId ZONE_ID_UTC = ZONE_UTC.toZoneId();

	/**
	 * {@link ZoneId}转换为{@link TimeZone}，{@code null}则返回系统默认值
	 *
	 * @param zoneId {@link ZoneId}，{@code null}则返回系统默认值
	 * @return {@link TimeZone}
	 */
	public static TimeZone toTimeZone(final ZoneId zoneId) {
		if (null == zoneId) {
			return TimeZone.getDefault();
		}

		return TimeZone.getTimeZone(zoneId);
	}

	/**
	 * {@link TimeZone}转换为{@link ZoneId}，{@code null}则返回系统默认值
	 *
	 * @param timeZone {@link TimeZone}，{@code null}则返回系统默认值
	 * @return {@link ZoneId}
	 */
	public static ZoneId toZoneId(final TimeZone timeZone) {
		if (null == timeZone) {
			return ZoneId.systemDefault();
		}

		return timeZone.toZoneId();
	}
}
