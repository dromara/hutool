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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

import java.sql.Timestamp;

/**
 * {@code java.sql.*}日期时间相关封装<br>
 * 考虑到JDK9+模块化后，java.sql并非默认引入模块，因此将相关内容单独封装为工具，避免类找不到问题。
 *
 * @author looly
 * @since 6.0.0
 */
public class SqlDateUtil {

	/**
	 * 转为{@link Timestamp}
	 *
	 * @param date 日期时间，非空
	 * @return {@link Timestamp}
	 */
	public static Timestamp timestamp(final java.util.Date date) {
		Assert.notNull(date);
		return new Timestamp(date.getTime());
	}

	/**
	 * /**
	 * 转为{@link java.sql.Date}
	 *
	 * @param date 日期时间，非空
	 * @return {@link java.sql.Date}
	 */
	public static java.sql.Date date(final java.util.Date date) {
		Assert.notNull(date);
		return new java.sql.Date(date.getTime());
	}

	/**
	 * /**
	 * 转为{@link java.sql.Time}
	 *
	 * @param date 日期时间，非空
	 * @return {@link java.sql.Time}
	 */
	public static java.sql.Time time(final java.util.Date date) {
		Assert.notNull(date);
		return new java.sql.Time(date.getTime());
	}

	/**
	 * 时间戳转为子类型，支持：
	 * <ul>
	 *     <li>{@link java.util.Date}</li>
	 *     <li>{@link DateTime}</li>
	 *     <li>{@link java.sql.Date}</li>
	 *     <li>{@link java.sql.Time}</li>
	 *     <li>{@link java.sql.Timestamp}</li>
	 * </ul>
	 *
	 * @param <T>         日期类型
	 * @param targetClass 目标类型
	 * @param mills       Date
	 * @return 目标类型对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends java.util.Date> T wrap(final Class<?> targetClass, final long mills) {
		// 返回指定类型
		if (java.sql.Date.class == targetClass) {
			return (T) new java.sql.Date(mills);
		}
		if (java.sql.Time.class == targetClass) {
			return (T) new java.sql.Time(mills);
		}
		if (java.sql.Timestamp.class == targetClass) {
			return (T) new Timestamp(mills);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupported target Date type: {}", targetClass.getName()));
	}
}
