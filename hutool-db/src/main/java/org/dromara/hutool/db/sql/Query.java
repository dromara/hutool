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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.Page;

import java.util.Collection;
import java.util.Set;

/**
 * 查询对象，用于传递查询所需的字段值<br>
 * 查询对象根据表名（可以多个），多个条件 {@link Condition} 构建查询对象完成查询。<br>
 * 如果想自定义返回结果，则可在查询对象中自定义要查询的字段名，分页{@link Page}信息来自定义结果。
 *
 * @author Looly
 */
public class Query implements Cloneable {

	/**
	 * 查询的字段名列表
	 */
	Collection<String> fields;
	/**
	 * 查询的表名
	 */
	String[] tableNames;
	/**
	 * 查询的条件语句
	 */
	Condition[] where;
	/**
	 * 分页对象
	 */
	Page page;

	/**
	 * 从{@link Entity}构建Query
	 *
	 * @param where 条件查询{@link Entity}，包含条件Map和表名
	 * @return Query
	 * @since 5.5.3
	 */
	public static Query of(final Entity where) {
		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		final Set<String> fieldNames = where.getFieldNames();
		if (CollUtil.isNotEmpty(fieldNames)) {
			query.setFields(fieldNames);
		}

		return query;
	}

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param tableNames 表名
	 */
	public Query(final String... tableNames) {
		this(null, tableNames);
		this.tableNames = tableNames;
	}

	/**
	 * 构造
	 *
	 * @param where      条件语句
	 * @param tableNames 表名
	 */
	public Query(final Condition[] where, final String... tableNames) {
		this(where, null, tableNames);
	}

	/**
	 * 构造
	 *
	 * @param where      条件语句
	 * @param page       分页
	 * @param tableNames 表名
	 */
	public Query(final Condition[] where, final Page page, final String... tableNames) {
		this(null, tableNames, where, page);
	}

	/**
	 * 构造
	 *
	 * @param fields     字段
	 * @param tableNames 表名
	 * @param where      条件
	 * @param page       分页
	 */
	public Query(final Collection<String> fields, final String[] tableNames, final Condition[] where, final Page page) {
		this.fields = fields;
		this.tableNames = tableNames;
		this.where = where;
		this.page = page;
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Getters and Setters start

	/**
	 * 获得查询的字段名列表
	 *
	 * @return 查询的字段名列表
	 */
	public Collection<String> getFields() {
		return fields;
	}

	/**
	 * 设置查询的字段名列表
	 *
	 * @param fields 查询的字段名列表
	 * @return this
	 */
	public Query setFields(final Collection<String> fields) {
		this.fields = fields;
		return this;
	}

	/**
	 * 设置查询的字段名列表
	 *
	 * @param fields 查询的字段名列表
	 * @return this
	 */
	public Query setFields(final String... fields) {
		this.fields = ListUtil.of(fields);
		return this;
	}

	/**
	 * 获得表名数组
	 *
	 * @return 表名数组
	 */
	public String[] getTableNames() {
		return tableNames;
	}

	/**
	 * 设置表名
	 *
	 * @param tableNames 表名
	 * @return this
	 */
	public Query setTableNames(final String... tableNames) {
		this.tableNames = tableNames;
		return this;
	}

	/**
	 * 获得条件语句
	 *
	 * @return 条件语句
	 */
	public Condition[] getWhere() {
		return where;
	}

	/**
	 * 设置条件语句
	 *
	 * @param where 条件语句
	 * @return this
	 */
	public Query setWhere(final Condition... where) {
		this.where = where;
		return this;
	}

	/**
	 * 获得分页对象，无分页返回{@code null}
	 *
	 * @return 分页对象 or {@code null}
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * 设置分页对象
	 *
	 * @param page 分页对象
	 * @return this
	 */
	public Query setPage(final Page page) {
		this.page = page;
		return this;
	}
	// --------------------------------------------------------------- Getters and Setters end

	/**
	 * 获得第一个表名
	 *
	 * @return 表名
	 * @throws DbException 没有表
	 */
	public String getFirstTableName() throws DbException {
		if (ArrayUtil.isEmpty(this.tableNames)) {
			throw new DbException("No tableName!");
		}
		return this.tableNames[0];
	}

	@Override
	public Query clone() {
		try {
			return (Query) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
