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
