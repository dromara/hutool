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

package org.dromara.hutool.db.meta;


import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据库表的索引信息<br>
 * 如果时单列索引，只有一个{@link ColumnIndexInfo}，联合索引则拥有多个{@link ColumnIndexInfo}
 *
 * @author huzhongying
 */
public class IndexInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 索引值是否可以不唯一
	 */
	private boolean nonUnique;

	/**
	 * 索引名称
	 */
	private String indexName;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * table所在的schema
	 */
	private String schema;
	/**
	 * table所在的catalog
	 */
	private String catalog;

	/**
	 * 索引中的列信息,按索引顺序排列
	 */
	private List<ColumnIndexInfo> columnIndexInfoList;

	/**
	 * 构造
	 *
	 * @param nonUnique 索引值是否可以不唯一
	 * @param indexName 索引名称
	 * @param tableName 表名
	 * @param schema    table所在的schema
	 * @param catalog   table所在的catalog
	 */
	public IndexInfo(final boolean nonUnique, final String indexName, final String tableName, final String schema, final String catalog) {
		this.nonUnique = nonUnique;
		this.indexName = indexName;
		this.tableName = tableName;
		this.schema = schema;
		this.catalog = catalog;
		this.setColumnIndexInfoList(new ArrayList<>());
	}

	public boolean isNonUnique() {
		return nonUnique;
	}

	public void setNonUnique(final boolean nonUnique) {
		this.nonUnique = nonUnique;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(final String indexName) {
		this.indexName = indexName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(final String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(final String catalog) {
		this.catalog = catalog;
	}

	public List<ColumnIndexInfo> getColumnIndexInfoList() {
		return columnIndexInfoList;
	}

	public void setColumnIndexInfoList(final List<ColumnIndexInfo> columnIndexInfoList) {
		this.columnIndexInfoList = columnIndexInfoList;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final IndexInfo indexInfo = (IndexInfo) o;
		return ObjUtil.equals(indexName, indexInfo.indexName)
				&& ObjUtil.equals(tableName, indexInfo.tableName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(indexName, tableName);
	}

	@Override
	public IndexInfo clone() throws CloneNotSupportedException {
		return (IndexInfo) super.clone();
	}

	@Override
	public String toString() {
		return "IndexInfo{" +
				"nonUnique=" + nonUnique +
				", indexName='" + indexName + '\'' +
				", tableName='" + tableName + '\'' +
				", schema='" + schema + '\'' +
				", catalog='" + catalog + '\'' +
				", columnIndexInfoList=" + columnIndexInfoList +
				'}';
	}
}
