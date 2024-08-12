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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 排序方式（升序或者降序）
 *
 * @author Looly
 */
public enum Direction {
	/**
	 * 升序
	 */
	ASC,
	/**
	 * 降序
	 */
	DESC;

	/**
	 * 根据字符串值返回对应Direction值
	 *
	 * @param value 排序方式字符串，只能是 ASC或DESC
	 * @return Direction，{@code null}表示提供的value为空
	 * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
	 */
	public static Direction fromString(final String value) throws IllegalArgumentException {
		if (StrUtil.isEmpty(value)) {
			return null;
		}

		// 兼容元数据中ASC和DESC表示
		if (1 == value.length()) {
			if ("A".equalsIgnoreCase(value)) {
				return ASC;
			} else if ("D".equalsIgnoreCase(value)) {
				return DESC;
			}
		}

		try {
			return Direction.valueOf(value.toUpperCase());
		} catch (final Exception e) {
			throw new IllegalArgumentException(StrUtil.format(
					"Invalid value [{}] for orders given!Has to be either 'desc' or 'asc' (case insensitive).", value), e);
		}
	}
}
