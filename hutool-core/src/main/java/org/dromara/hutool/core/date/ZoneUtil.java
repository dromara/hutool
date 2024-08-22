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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

	/**
	 * 获取指定偏移量的可用时区，如果有多个时区匹配，使用第一个
	 *
	 * @param rawOffset 偏移量
	 * @param timeUnit  偏移量单位
	 * @return 时区
	 */
	public static TimeZone getTimeZoneByOffset(final int rawOffset, final TimeUnit timeUnit) {
		final String id = getAvailableID(rawOffset, timeUnit);
		return null == id ? null : TimeZone.getTimeZone(id);
	}

	/**
	 * 获取指定偏移量的可用时区ID，如果有多个时区匹配，使用第一个
	 *
	 * @param rawOffset 偏移量
	 * @param timeUnit  偏移量单位
	 * @return 时区ID，未找到返回{@code null}
	 */
	public static String getAvailableID(final int rawOffset, final TimeUnit timeUnit) {
		final String[] availableIDs = TimeZone.getAvailableIDs(
			(int) ObjUtil.defaultIfNull(timeUnit, TimeUnit.MILLISECONDS).toMillis(rawOffset));
		return ArrayUtil.isEmpty(availableIDs) ? null : availableIDs[0];
	}
}
