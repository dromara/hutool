package cn.hutool.db.meta;

import cn.hutool.db.DbRuntimeException;

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
	 * @throws DbRuntimeException SQLException包装
	 */
	public ResultColumn(final ResultSetMetaData metaData, final int columnIndexBase1) throws DbRuntimeException {
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
			throw new DbRuntimeException(e);
		}
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public boolean isCurrency() {
		return currency;
	}

	public int getNullable() {
		return nullable;
	}

	public ColumnNullable getNullableEnum() {
		return ColumnNullable.of(getNullable());
	}

	public boolean isSigned() {
		return signed;
	}

	public int getDisplaySize() {
		return displaySize;
	}

	public String getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public int getPrecision() {
		return precision;
	}

	public int getScale() {
		return scale;
	}

	public String getTableName() {
		return tableName;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public int getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isWritable() {
		return writable;
	}

	public boolean isDefinitelyWritable() {
		return definitelyWritable;
	}

	public String getClassName() {
		return className;
	}

	public enum ColumnNullable {
		NO_NULLS(ResultSetMetaData.columnNoNulls),
		NULLABLE(ResultSetMetaData.columnNullable),
		UNKNOWN(ResultSetMetaData.columnNullableUnknown);

		/**
		 * ResultSetMetaData中的int值转枚举
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
	}
}
