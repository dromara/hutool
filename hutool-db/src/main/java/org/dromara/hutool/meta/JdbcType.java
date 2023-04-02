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

package org.dromara.hutool.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * JDBC中字段类型枚举
 *
 * @author Clinton Begin
 * @see java.sql.Types
 */
public enum JdbcType {
	/**
	 * {@link java.sql.Types#ARRAY}
	 */
	ARRAY(java.sql.Types.ARRAY), //
	/**
	 * {@link java.sql.Types#BIT}
	 */
	BIT(java.sql.Types.BIT), //
	/**
	 * {@link java.sql.Types#TINYINT}
	 */
	TINYINT(java.sql.Types.TINYINT), //
	/**
	 * {@link java.sql.Types#SMALLINT}
	 */
	SMALLINT(java.sql.Types.SMALLINT), //
	/**
	 * {@link java.sql.Types#INTEGER}
	 */
	INTEGER(java.sql.Types.INTEGER), //
	/**
	 * {@link java.sql.Types#BIGINT}
	 */
	BIGINT(java.sql.Types.BIGINT), //
	/**
	 * {@link java.sql.Types#FLOAT}
	 */
	FLOAT(java.sql.Types.FLOAT), //
	/**
	 * {@link java.sql.Types#REAL}
	 */
	REAL(java.sql.Types.REAL), //
	/**
	 * {@link java.sql.Types#DOUBLE}
	 */
	DOUBLE(java.sql.Types.DOUBLE), //
	/**
	 * {@link java.sql.Types#NUMERIC}
	 */
	NUMERIC(java.sql.Types.NUMERIC), //
	/**
	 * {@link java.sql.Types#DECIMAL}
	 */
	DECIMAL(java.sql.Types.DECIMAL), //
	/**
	 * {@link java.sql.Types#CHAR}
	 */
	CHAR(java.sql.Types.CHAR), //
	/**
	 * {@link java.sql.Types#VARCHAR}
	 */
	VARCHAR(java.sql.Types.VARCHAR), //
	/**
	 * {@link java.sql.Types#LONGVARCHAR}
	 */
	LONGVARCHAR(java.sql.Types.LONGVARCHAR), //
	/**
	 * {@link java.sql.Types#DATE}
	 */
	DATE(java.sql.Types.DATE), //
	/**
	 * {@link java.sql.Types#TIME}
	 */
	TIME(java.sql.Types.TIME), //
	/**
	 * {@link java.sql.Types#TIMESTAMP}
	 */
	TIMESTAMP(java.sql.Types.TIMESTAMP), //
	/**
	 * {@link java.sql.Types#BINARY}
	 */
	BINARY(java.sql.Types.BINARY), //
	/**
	 * {@link java.sql.Types#VARBINARY}
	 */
	VARBINARY(java.sql.Types.VARBINARY), //
	/**
	 * {@link java.sql.Types#LONGVARBINARY}
	 */
	LONGVARBINARY(java.sql.Types.LONGVARBINARY), //
	/**
	 * {@link java.sql.Types#NULL}
	 */
	NULL(java.sql.Types.NULL), //
	/**
	 * {@link java.sql.Types#OTHER}
	 */
	OTHER(java.sql.Types.OTHER), //
	/**
	 * {@link java.sql.Types#BLOB}
	 */
	BLOB(java.sql.Types.BLOB), //
	/**
	 * {@link java.sql.Types#CLOB}
	 */
	CLOB(java.sql.Types.CLOB), //
	/**
	 * {@link java.sql.Types#BOOLEAN}
	 */
	BOOLEAN(java.sql.Types.BOOLEAN), //
	/**
	 * Oracle Cursor
	 */
	CURSOR(-10), // Oracle
	/**
	 * UNDEFINED
	 */
	UNDEFINED(Integer.MIN_VALUE + 1000), //
	/**
	 * {@link java.sql.Types#NVARCHAR}
	 */
	NVARCHAR(java.sql.Types.NVARCHAR), // JDK6
	/**
	 * {@link java.sql.Types#NCHAR}
	 */
	NCHAR(java.sql.Types.NCHAR), // JDK6
	/**
	 * {@link java.sql.Types#NCLOB}
	 */
	NCLOB(java.sql.Types.NCLOB), // JDK6
	/**
	 * {@link java.sql.Types#STRUCT}
	 */
	STRUCT(java.sql.Types.STRUCT),
	/**
	 * {@link java.sql.Types#JAVA_OBJECT}
	 */
	JAVA_OBJECT(java.sql.Types.JAVA_OBJECT),
	/**
	 * {@link java.sql.Types#DISTINCT}
	 */
	DISTINCT(java.sql.Types.DISTINCT),
	/**
	 * {@link java.sql.Types#REF}
	 */
	REF(java.sql.Types.REF),
	/**
	 * {@link java.sql.Types#BOOLEAN}
	 */
	DATALINK(java.sql.Types.DATALINK), //
	/**
	 * {@link java.sql.Types#ROWID}
	 */
	ROWID(java.sql.Types.ROWID), // JDK6
	/**
	 * {@link java.sql.Types#LONGNVARCHAR}
	 */
	LONGNVARCHAR(java.sql.Types.LONGNVARCHAR), // JDK6
	/**
	 * {@link java.sql.Types#SQLXML}
	 */
	SQLXML(java.sql.Types.SQLXML), // JDK6
	/**
	 * SQL Server 2008 DateTimeOffset
	 */
	DATETIMEOFFSET(-155), // SQL Server 2008
	/**
	 * Time With TimeZone
	 */
	TIME_WITH_TIMEZONE(2013), // JDBC 4.2 JDK8
	/**
	 * TimeStamp With TimeZone
	 */
	TIMESTAMP_WITH_TIMEZONE(2014); // JDBC 4.2 JDK8

	// 此处无写操作，使用HashMap没有线程安全问题
	private static final Map<Integer, JdbcType> CODE_MAP = new HashMap<>(128, 1);

	static {
		for (final JdbcType type : JdbcType.values()) {
			CODE_MAP.put(type.value, type);
		}
	}

	/**
	 * 通过{@link java.sql.Types}中对应int值找到enum值
	 *
	 * @param code Jdbc type值
	 * @return {@code JdbcType}
	 */
	public static JdbcType valueOf(final int code) {
		return CODE_MAP.get(code);
	}

	private final int value;

	/**
	 * 构造
	 *
	 * @param code {@link java.sql.Types} 中对应的值
	 */
	JdbcType(final int code) {
		this.value = code;
	}

	/**
	 * 获取枚举值，即JDBC字段类型代码
	 *
	 * @return 字段类型代码
	 */
	public int getValue() {
		return this.value;
	}
}
