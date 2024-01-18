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

package org.dromara.hutool.db.meta;

import org.dromara.hutool.db.DbException;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * {@link ResultSetMetaData}中某一列的元数据信息
 *
 * @author looly
 */
public class ResultColumn {

	private final boolean autoIncrement;
	private final boolean caseSensitive;
	private final boolean searchable;
	private final boolean currency;
	private final int nullable;
	private final boolean signed;
	private final int displaySize;
	private final String label;
	private final String name;
	private final String schemaName;
	private final int precision;
	private final int scale;
	private final String tableName;
	private final String catalogName;
	private final int type;
	private final String typeName;
	private final boolean readOnly;
	private final boolean writable;
	private final boolean definitelyWritable;
	private final String className;

	/**
	 * 构造
	 *
	 * @param metaData         {@link ResultSetMetaData}
	 * @param columnIndexBase1 列序号，从1开始。即第一列为1，第二列为2。。。
	 * @throws DbException SQLException包装
	 */
	public ResultColumn(final ResultSetMetaData metaData, final int columnIndexBase1) throws DbException {
		try {
			this.autoIncrement = metaData.isAutoIncrement(columnIndexBase1);
			this.caseSensitive = metaData.isCaseSensitive(columnIndexBase1);
			this.searchable = metaData.isSearchable(columnIndexBase1);
			this.currency = metaData.isCurrency(columnIndexBase1);
			this.nullable = metaData.isNullable(columnIndexBase1);
			this.signed = metaData.isSigned(columnIndexBase1);
			this.displaySize = metaData.getColumnDisplaySize(columnIndexBase1);
			this.label = metaData.getColumnLabel(columnIndexBase1);
			this.name = metaData.getColumnName(columnIndexBase1);
			this.schemaName = metaData.getSchemaName(columnIndexBase1);
			this.precision = metaData.getPrecision(columnIndexBase1);
			this.scale = metaData.getScale(columnIndexBase1);
			this.tableName = metaData.getTableName(columnIndexBase1);
			this.catalogName = metaData.getCatalogName(columnIndexBase1);
			this.type = metaData.getColumnType(columnIndexBase1);
			this.typeName = metaData.getColumnTypeName(columnIndexBase1);
			this.readOnly = metaData.isReadOnly(columnIndexBase1);
			this.writable = metaData.isWritable(columnIndexBase1);
			this.definitelyWritable = metaData.isDefinitelyWritable(columnIndexBase1);
			this.className = metaData.getColumnClassName(columnIndexBase1);
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 是否自增
	 *
	 * @return 是否自增
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * 是否大小写敏感
	 *
	 * @return 是否大小写敏感
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * 是否可搜索
	 *
	 * @return 是否可搜索
	 */
	public boolean isSearchable() {
		return searchable;
	}

	/**
	 * 是否为货币
	 *
	 * @return 是否为货币
	 */
	public boolean isCurrency() {
		return currency;
	}

	/**
	 * 获取null值选项，即是否可以为{@code null}
	 *
	 * @return null值选项，是否可以为{@code null}
	 */
	public int getNullable() {
		return nullable;
	}

	/**
	 * 获取列null值选项枚举，即是否可以为{@code null}
	 *
	 * @return 列null值选项枚举，即是否可以为{@code null}
	 */
	public ColumnNullable getNullableEnum() {
		return ColumnNullable.of(getNullable());
	}

	/**
	 * 是否为带正负号的数字
	 *
	 * @return 是否为带正负号的数字
	 */
	public boolean isSigned() {
		return signed;
	}

	/**
	 * 获取正常最大宽度（以字符数计）
	 *
	 * @return 正常最大宽度
	 */
	public int getDisplaySize() {
		return displaySize;
	}

	/**
	 * 获取列标签
	 *
	 * @return 标签
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 获取列名称
	 *
	 * @return 列名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取表架构名称
	 *
	 * @return 表架构名称
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * 获取小数位数
	 *
	 * @return 小数位数
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * 小数点右侧的位数
	 *
	 * @return 小数点右侧的位数
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * 获取表名
	 *
	 * @return 表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 获取表的目录名称
	 *
	 * @return 表的目录名称
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * 获取SQL类型
	 *
	 * @return SQL类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 获取类型名称
	 *
	 * @return 类型名称
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 是否只读（不可写入）
	 *
	 * @return 是否只读（不可写入）
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * 是否能够成功在指定列上写入
	 *
	 * @return 是否能够成功在指定列上写入
	 */
	public boolean isWritable() {
		return writable;
	}

	/**
	 * 写入操作是否将一定成功
	 *
	 * @return 写入操作是否将一定成功
	 */
	public boolean isDefinitelyWritable() {
		return definitelyWritable;
	}

	/**
	 * 如果调用getObject方法来从列中检索值，则返回生成其实例的 Java 类的完全限定名称
	 *
	 * @return 包含类的完全限定名称
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 列null值选项
	 */
	public enum ColumnNullable {
		/**
		 * 列不允许有null
		 */
		NO_NULLS(ResultSetMetaData.columnNoNulls),
		/**
		 * 列允许有null
		 */
		NULLABLE(ResultSetMetaData.columnNullable),
		/**
		 * 未知
		 */
		UNKNOWN(ResultSetMetaData.columnNullableUnknown);

		/**
		 * ResultSetMetaData中的int值转枚举
		 *
		 * @param nullable nullable值
		 * @return ColumnNullable
		 */
		public static ColumnNullable of(final int nullable) {
			switch (nullable) {
				case ResultSetMetaData.columnNoNulls:
					return NO_NULLS;
				case ResultSetMetaData.columnNullable:
					return NULLABLE;
				default:
					return UNKNOWN;
			}
		}

		final int value;

		ColumnNullable(final int value) {
			this.value = value;
		}

		/**
		 * 获取枚举值，即列null值选项代码
		 *
		 * @return 列null值选项代码
		 */
		public int getValue() {
			return this.value;
		}
	}
}
