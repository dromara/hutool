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

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.util.BooleanUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.db.DbException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库表的列信息
 *
 * @author loolly
 */
public class Column implements Serializable, Cloneable {
	private static final long serialVersionUID = 577527740359719367L;

	// ----------------------------------------------------- Fields start
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 列名
	 */
	private String name;
	/**
	 * 数据库字段类型，包括长度
	 */
	private ColumnType type;

	/**
	 * 保留小数位数
	 */
	private Integer digit;
	/**
	 * 是否为可空
	 */
	private boolean isNullable;
	/**
	 * 注释
	 */
	private String remarks;
	/**
	 * 是否自增
	 */
	private boolean autoIncrement;
	/**
	 * 字段默认值<br>
	 * default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be {@code null})
	 */
	private String columnDef;
	/**
	 * 是否为主键
	 */
	private boolean isPk;
	/**
	 * 列字段顺序
	 */
	private int order;
// ----------------------------------------------------- Fields end

	/**
	 * 创建列对象
	 *
	 * @param columnMetaRs 列元信息的ResultSet
	 * @param table        表信息
	 * @return 列对象
	 * @since 5.4.3
	 */
	public static Column of(final Table table, final ResultSet columnMetaRs) {
		return new Column(table, columnMetaRs);
	}

	// ----------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public Column() {
	}

	/**
	 * 构造
	 *
	 * @param table        表信息
	 * @param columnMetaRs Meta信息的ResultSet
	 * @since 5.4.3
	 */
	public Column(final Table table, final ResultSet columnMetaRs) {
		try {
			init(table, columnMetaRs);
		} catch (final SQLException e) {
			throw new DbException(e, "Get table [{}] meta info error!", tableName);
		}
	}
	// ----------------------------------------------------- Constructor end

	/**
	 * 初始化
	 *
	 * @param table        表信息
	 * @param columnMetaRs 列的meta ResultSet
	 * @throws SQLException SQL执行异常
	 */
	public void init(final Table table, final ResultSet columnMetaRs) throws SQLException {
		this.tableName = table.getTableName();

		this.name = columnMetaRs.getString("COLUMN_NAME");
		this.isPk = table.isPk(this.name);

		final int type = columnMetaRs.getInt("DATA_TYPE");
		String typeName = columnMetaRs.getString("TYPE_NAME");
		//issue#2201@Gitee
		typeName = ReUtil.delLast("\\(\\d+\\)", typeName);
		final long size = columnMetaRs.getLong("COLUMN_SIZE");
		this.type = new ColumnType(type, typeName, size);

		this.isNullable = columnMetaRs.getBoolean("NULLABLE");
		this.remarks = columnMetaRs.getString("REMARKS");
		this.columnDef = columnMetaRs.getString("COLUMN_DEF");
		this.order = columnMetaRs.getRow();
		// 保留小数位数
		try {
			this.digit = columnMetaRs.getInt("DECIMAL_DIGITS");
		} catch (final SQLException ignore) {
			//某些驱动可能不支持，跳过
		}

		// 是否自增
		try {
			final String auto = columnMetaRs.getString("IS_AUTOINCREMENT");
			if (BooleanUtil.toBoolean(auto)) {
				this.autoIncrement = true;
			}
		} catch (final SQLException ignore) {
			//某些驱动可能不支持，跳过
		}
	}

	// ----------------------------------------------------- Getters and Setters start

	/**
	 * 获取表名
	 *
	 * @return 表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名
	 *
	 * @param tableName 表名
	 * @return this
	 */
	public Column setTableName(final String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 获取列名
	 *
	 * @return 列名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置列名
	 *
	 * @param name 列名
	 * @return this
	 */
	public Column setName(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取数据库字段类型，包括长度
	 *
	 * @return 数据库字段类型，包括长度
	 */
	public ColumnType getType() {
		return type;
	}

	/**
	 * 设置数据库字段类型，包括长度
	 *
	 * @param type 数据库字段类型，包括长度
	 * @return this
	 */
	public Column setType(final ColumnType type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取小数位数
	 *
	 * @return 大小或数据长度
	 */
	public Integer getDigit() {
		return digit;
	}

	/**
	 * 设置小数位数
	 *
	 * @param digit 小数位数
	 * @return this
	 */
	public Column setDigit(final int digit) {
		this.digit = digit;
		return this;
	}

	/**
	 * 是否为可空
	 *
	 * @return 是否为可空
	 */
	public boolean isNullable() {
		return isNullable;
	}

	/**
	 * 设置是否为可空
	 *
	 * @param isNullable 是否为可空
	 * @return this
	 */
	public Column setNullable(final boolean isNullable) {
		this.isNullable = isNullable;
		return this;
	}

	/**
	 * 获取注释
	 *
	 * @return 注释
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置注释
	 *
	 * @param remarks 注释
	 * @return this
	 */
	public Column setRemarks(final String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * 是否自增
	 *
	 * @return 是否自增
	 * @since 5.4.3
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * 设置是否自增
	 *
	 * @param autoIncrement 是否自增
	 * @return this
	 * @since 5.4.3
	 */
	public Column setAutoIncrement(final boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
		return this;
	}

	/**
	 * 获取默认值
	 *
	 * @return 默认值
	 */
	public String getColumnDef() {
		return columnDef;
	}

	/**
	 * 设置默认值
	 *
	 * @param columnDef 默认值
	 * @return this
	 */
	public Column setColumnDef(final String columnDef) {
		this.columnDef = columnDef;
		return this;
	}

	/**
	 * 是否主键
	 *
	 * @return 是否主键
	 * @since 5.4.3
	 */
	public boolean isPk() {
		return isPk;
	}

	/**
	 * 设置是否主键
	 *
	 * @param isPk 是否主键
	 * @return this
	 * @since 5.4.3
	 */
	public Column setPk(final boolean isPk) {
		this.isPk = isPk;
		return this;
	}

	/**
	 * 获取顺序号
	 *
	 * @return 顺序号
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * 设置顺序号
	 *
	 * @param order 顺序号
	 * @return this
	 */
	public Column setOrder(final int order) {
		this.order = order;
		return this;
	}

	// ----------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", isNullable=" + isNullable + ", order=" + order + "]";
	}

	@Override
	public Column clone() throws CloneNotSupportedException {
		return (Column) super.clone();
	}
}
