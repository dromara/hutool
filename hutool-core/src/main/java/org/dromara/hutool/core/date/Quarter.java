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

/**
 * 季度枚举
 *
 * @author zhfish(https : / / github.com / zhfish)
 * @see #Q1
 * @see #Q2
 * @see #Q3
 * @see #Q4
 */
public enum Quarter {

	/**
	 * 第一季度
	 */
	Q1(1),
	/**
	 * 第二季度
	 */
	Q2(2),
	/**
	 * 第三季度
	 */
	Q3(3),
	/**
	 * 第四季度
	 */
	Q4(4);

	// ---------------------------------------------------------------
	private final int value;

	Quarter(final int value) {
		this.value = value;
	}

	/**
	 * 获取季度值
	 *
	 * @return 季度值
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 将 季度int转换为Season枚举对象<br>
	 *
	 * @param intValue 季度int表示
	 * @return {@code Quarter}
	 * @see #Q1
	 * @see #Q2
	 * @see #Q3
	 * @see #Q4
	 */
	public static Quarter of(final int intValue) {
		switch (intValue) {
			case 1:
				return Q1;
			case 2:
				return Q2;
			case 3:
				return Q3;
			case 4:
				return Q4;
			default:
				return null;
		}
	}
}
