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

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 索引中的列信息
 *
 * @author huzhongying
 * @since 5.7.23
 */
public class ColumnIndexInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 根据DatabaseMetaData#getIndexInfo获取的{@link ResultSet}构建索引列信息
	 *
	 * @param rs 结果集，通过DatabaseMetaData#getIndexInfo获取
	 * @return ColumnIndexInfo
	 */
	public static ColumnIndexInfo of(final ResultSet rs) {
		try {
			return new ColumnIndexInfo(
					rs.getString("COLUMN_NAME"),
					rs.getString("ASC_OR_DESC"));
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 列排序顺序，“A”: 升序，“D” : 降序，如果不支持排序顺序，可能为空
	 */
	private String ascOrDesc;

	/**
	 * 构造
	 *
	 * @param columnName 索引列名
	 * @param ascOrDesc  正序或反序，null表示无顺序表示
	 */
	public ColumnIndexInfo(final String columnName, final String ascOrDesc) {
		this.columnName = columnName;
		this.ascOrDesc = ascOrDesc;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getAscOrDesc() {
		return ascOrDesc;
	}

	public void setAscOrDesc(final String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}

	@Override
	public ColumnIndexInfo clone() throws CloneNotSupportedException {
		return (ColumnIndexInfo) super.clone();
	}

	@Override
	public String toString() {
		return "ColumnIndexInfo{" +
				"columnName='" + columnName + '\'' +
				", ascOrDesc='" + ascOrDesc + '\'' +
				'}';
	}
}
