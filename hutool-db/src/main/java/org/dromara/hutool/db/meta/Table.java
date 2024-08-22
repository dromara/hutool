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

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库表信息
 *
 * @author loolly
 */
public class Table implements Serializable, Cloneable {
	private static final long serialVersionUID = -810699625961392983L;

	/**
	 * table所在的schema
	 */
	private String schema;
	/**
	 * tables所在的catalog
	 */
	private String catalog;
	/**
	 * 表名，特殊表名一般带包装符号，如："1234"
	 */
	private String tableName;
	/**
	 * 表名（无包装符号），如"1234"对应的pureTableName为1234
	 */
	private String pureTableName;
	/**
	 * 注释
	 */
	private String remarks;
	/**
	 * 主键字段名列表
	 */
	private Set<String> pkNames = new LinkedHashSet<>();
	/**
	 * 索引信息
	 */
	private List<IndexInfo> indexInfoList;
	/**
	 * 列映射，列名-列对象
	 */
	private final Map<String, Column> columns = new LinkedHashMap<>();

	/**
	 * 根据提供的表名创建一个新的Table实例。
	 *
	 * @param tableName 表的名称，用于标识数据库中的特定表。
	 * @return 返回一个新的Table实例，其名称为传入的表名。
	 */
	public static Table of(final String tableName) {
		return new Table(tableName);
	}

	// ----------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param tableName 表名
	 */
	public Table(final String tableName) {
		this.setTableName(tableName);
	}
	// ----------------------------------------------------- Constructor end

	// ----------------------------------------------------- Getters and Setters start

	/**
	 * 获取 schema
	 *
	 * @return schema
	 * @since 5.4.3
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * 设置schema
	 *
	 * @param schema schema
	 * @return this
	 * @since 5.4.3
	 */
	public Table setSchema(final String schema) {
		this.schema = schema;
		return this;
	}

	/**
	 * 获取catalog
	 *
	 * @return catalog
	 * @since 5.4.3
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * 设置catalog
	 *
	 * @param catalog catalog
	 * @return this
	 * @since 5.4.3
	 */
	public Table setCatalog(final String catalog) {
		this.catalog = catalog;
		return this;
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
	 * 设置表名
	 *
	 * @param tableName 表名
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取表名（无包装符号），如"1234"对应的pureTableName为1234
	 *
	 * @return 表名（无包装符号）
	 */
	public String getPureTableName() {
		return pureTableName;
	}

	/**
	 * 设置表名（无包装符号），如"1234"对应的pureTableName为1234
	 *
	 * @param pureTableName 表名
	 */
	public void setPureTableName(final String pureTableName) {
		this.pureTableName = pureTableName;
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
	public Table setRemarks(final String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * 获取主键列表
	 *
	 * @return 主键列表
	 */
	public Set<String> getPkNames() {
		return pkNames;
	}

	/**
	 * 给定列名是否为主键
	 *
	 * @param columnName 列名
	 * @return 是否为主键
	 * @since 5.4.3
	 */
	public boolean isPk(final String columnName) {
		return getPkNames().contains(columnName);
	}

	/**
	 * 设置主键列表
	 *
	 * @param pkNames 主键列表
	 */
	public void setPkNames(final Set<String> pkNames) {
		this.pkNames = pkNames;
	}
	// ----------------------------------------------------- Getters and Setters end

	/**
	 * 设置列对象
	 *
	 * @param column 列对象
	 * @return 自己
	 */
	public Table addColumn(final Column column) {
		this.columns.put(column.getName(), column);
		return this;
	}

	/**
	 * 获取某列信息
	 *
	 * @param name 列名
	 * @return 列对象
	 * @since 4.2.2
	 */
	public Column getColumn(final String name) {
		return this.columns.get(name);
	}

	/**
	 * 获取所有字段元信息
	 *
	 * @return 字段元信息集合
	 * @since 4.5.8
	 */
	public Collection<Column> getColumns() {
		return this.columns.values();
	}

	/**
	 * 添加主键
	 *
	 * @param pkColumnName 主键的列名
	 * @return 自己
	 */
	public Table addPk(final String pkColumnName) {
		this.pkNames.add(pkColumnName);
		return this;
	}

	/**
	 * 获取索引信息
	 *
	 * @return 索引信息
	 * @since 5.7.23
	 */
	public List<IndexInfo> getIndexInfoList() {
		return indexInfoList;
	}

	/**
	 * 设置索引信息
	 *
	 * @param indexInfoList 索引信息列表
	 * @since 5.7.23
	 */
	public void setIndexInfoList(final List<IndexInfo> indexInfoList) {
		this.indexInfoList = indexInfoList;
	}

	@Override
	public Table clone() throws CloneNotSupportedException {
		return (Table) super.clone();
	}
}
