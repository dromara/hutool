package com.xiaoleilu.hutool.db.sql;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 排序方式（升序或者降序）
 * @author Looly
 *
 */
public enum Direction{
	/** 升序 */
	ASC,
	/** 降序 */
	DESC;
	
	/**
	 * Returns the {@link Direction} enum for the given {@link String} value.
	 * 
	 * @param value
	 * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
	 * @return {@link Direction}
	 */
	public static Direction fromString(String value) {

		try {
			return Direction.valueOf(value.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException(StrUtil.format(
					"Invalid value [{}] for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
		}
	}
}
