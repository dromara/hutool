package cn.hutool.db.sql;

import cn.hutool.core.util.StrUtil;

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
	public static Direction fromString(String value) throws IllegalArgumentException {
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
		} catch (Exception e) {
			throw new IllegalArgumentException(StrUtil.format(
					"Invalid value [{}] for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
		}
	}
}
