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
