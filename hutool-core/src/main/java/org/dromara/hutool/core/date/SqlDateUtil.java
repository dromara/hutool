/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Assert;

import java.sql.Timestamp;
import java.util.Date;

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
	public static Timestamp timestamp(final Date date) {
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
	public static java.sql.Date date(final Date date) {
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
	public static java.sql.Time time(final Date date) {
		Assert.notNull(date);
		return new java.sql.Time(date.getTime());
	}

}
