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

import java.time.LocalTime;

/**
 * 针对 {@link LocalTime} 封装的一些工具方法
 *
 * @author Looly
 * @since 6.0.0
 */
public class LocalTimeUtil {
	/**
	 * 只有时分秒的最大时间
	 */
	public static final LocalTime MAX_HMS = LocalTime.of(23, 59, 59);

	/**
	 * 获取最大时间，提供参数是否将毫秒归零
	 * <ul>
	 *     <li>如果{@code truncateMillisecond}为{@code false}，返回时间最大值，为：23:59:59,999</li>
	 *     <li>如果{@code truncateMillisecond}为{@code true}，返回时间最大值，为：23:59:59,000</li>
	 * </ul>
	 *
	 * @param truncateMillisecond 是否毫秒归零
	 * @return {@link LocalTime}时间最大值
	 */
	public static LocalTime max(final boolean truncateMillisecond) {
		return truncateMillisecond ? MAX_HMS : LocalTime.MAX;
	}
}
