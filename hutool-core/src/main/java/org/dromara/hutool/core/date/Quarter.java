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
