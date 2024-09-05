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
		this.columnIndexInfoList = new ArrayList<>();
	}

	/**
	 * 检查索引是否是非唯一的
	 *
	 * @return 如果索引是非唯一的，返回true；否则返回false
	 */
	public boolean isNonUnique() {
		return nonUnique;
	}

	/**
	 * 设置索引的非唯一状态
	 *
	 * @param nonUnique 索引的非唯一状态
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setNonUnique(final boolean nonUnique) {
		this.nonUnique = nonUnique;
		return this;
	}

	/**
	 * 获取索引名称
	 *
	 * @return 索引名称
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * 设置索引名称
	 *
	 * @param indexName 索引名称
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setIndexName(final String indexName) {
		this.indexName = indexName;
		return this;
	}

	/**
	 * 获取表名称
	 *
	 * @return 表名称
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名称
	 *
	 * @param tableName 表名称
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setTableName(final String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 获取 schema 名称
	 *
	 * @return schema 名称
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * 设置 schema 名称
	 *
	 * @param schema schema 名称
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setSchema(final String schema) {
		this.schema = schema;
		return this;
	}

	/**
	 * 获取目录名称
	 *
	 * @return 目录名称
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * 设置目录名称
	 *
	 * @param catalog 目录名称
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setCatalog(final String catalog) {
		this.catalog = catalog;
		return this;
	}

	/**
	 * 获取列索引信息列表
	 *
	 * @return 列索引信息列表
	 */
	public List<ColumnIndexInfo> getColumnIndexInfoList() {
		return columnIndexInfoList;
	}

	/**
	 * 设置列索引信息列表
	 *
	 * @param columnIndexInfoList 列索引信息列表
	 * @return 当前的IndexInfo对象，以支持链式调用
	 */
	public IndexInfo setColumnIndexInfoList(final List<ColumnIndexInfo> columnIndexInfoList) {
		this.columnIndexInfoList = columnIndexInfoList;
		return this;
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
