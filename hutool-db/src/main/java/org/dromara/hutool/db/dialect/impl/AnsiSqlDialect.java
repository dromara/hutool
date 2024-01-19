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

package org.dromara.hutool.db.dialect.impl;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.sql.StatementUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.Condition;
import org.dromara.hutool.db.sql.Query;
import org.dromara.hutool.db.sql.QuoteWrapper;
import org.dromara.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * ANSI SQL 方言
 *
 * @author loolly
 */
public class AnsiSqlDialect implements Dialect {
	private static final long serialVersionUID = 2088101129774974580L;

	protected DbConfig dbConfig;
	protected QuoteWrapper quoteWrapper = new QuoteWrapper();

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public AnsiSqlDialect(final DbConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	public QuoteWrapper getWrapper() {
		return this.quoteWrapper;
	}

	@Override
	public void setWrapper(final QuoteWrapper quoteWrapper) {
		this.quoteWrapper = quoteWrapper;
	}

	@Override
	public PreparedStatement psForInsert(final boolean returnGeneratedKey, final Connection conn, final Entity entity) {
		final SqlBuilder insert = SqlBuilder.of(quoteWrapper).insert(entity, this.dialectName());

		return StatementUtil.prepareStatement(returnGeneratedKey, this.dbConfig, conn, insert.build(), insert.getParamValueArray());
	}

	@Override
	public PreparedStatement psForInsertBatch(final Connection conn, final Entity... entities) {
		if (ArrayUtil.isEmpty(entities)) {
			throw new DbException("Entities for batch insert is empty !");
		}
		// 批量，根据第一行数据结构生成SQL占位符
		final SqlBuilder insert = SqlBuilder.of(quoteWrapper).insert(entities[0], this.dialectName());
		return StatementUtil.prepareStatementForBatch(this.dbConfig, conn, insert.build(), entities);
	}

	@Override
	public PreparedStatement psForDelete(final Connection conn, final Query query) throws DbException {
		Assert.notNull(query, "query must be not null !");

		final Condition[] where = query.getWhere();
		if (ArrayUtil.isEmpty(where)) {
			// 对于无条件删除语句直接抛出异常禁止，防止误删除
			throw new DbException("No 'WHERE' condition, we can't prepared statement for delete everything.");
		}
		final SqlBuilder delete = SqlBuilder.of(quoteWrapper).delete(query.getFirstTableName()).where(where);
		return StatementUtil.prepareStatement(false, this.dbConfig, conn, delete.build(), delete.getParamValueArray());
	}

	@Override
	public PreparedStatement psForUpdate(final Connection conn, final Entity entity, final Query query) throws DbException {
		Assert.notNull(query, "query must be not null !");

		final Condition[] where = query.getWhere();
		if (ArrayUtil.isEmpty(where)) {
			// 对于无条件地删除语句直接抛出异常禁止，防止误删除
			throw new DbException("No 'WHERE' condition, we can't prepare statement for update everything.");
		}

		final SqlBuilder update = SqlBuilder.of(quoteWrapper).update(entity).where(where);

		return StatementUtil.prepareStatement(false, this.dbConfig, conn, update.build(), update.getParamValueArray());
	}

	@Override
	public PreparedStatement psForFind(final Connection conn, final Query query) {
		return psForPage(conn, query);
	}

	@Override
	public PreparedStatement psForPage(final Connection conn, final Query query) {
		Assert.notNull(query, "query must be not null !");
		if (ArrayUtil.hasBlank(query.getTableNames())) {
			throw new DbException("Table name must be not empty !");
		}

		final SqlBuilder find = SqlBuilder.of(quoteWrapper).query(query);
		return psForPage(conn, find, query.getPage());
	}

	@Override
	public PreparedStatement psForPage(final Connection conn, SqlBuilder sqlBuilder, final Page page) {
		// 根据不同数据库在查询SQL语句基础上包装其分页的语句
		if (null != page) {
			sqlBuilder = wrapPageSql(sqlBuilder.orderBy(page.getOrders()), page);
		}
		return StatementUtil.prepareStatement(false, this.dbConfig, conn, sqlBuilder.build(), sqlBuilder.getParamValueArray());
	}

	/**
	 * 根据不同数据库在查询SQL语句基础上包装其分页的语句<br>
	 * 各自数据库通过重写此方法实现最小改动情况下修改分页语句
	 *
	 * @param find 标准查询语句
	 * @param page 分页对象
	 * @return 分页语句
	 * @since 3.2.3
	 */
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		// limit A offset B 表示：A就是你需要多少行，B就是查询的起点位置。
		return find
			.append(" limit ")
			.append(page.getPageSize())
			.append(" offset ")
			.append(page.getStartPosition());
	}

	@Override
	public String dialectName() {
		return DialectName.ANSI.name();
	}

	// ---------------------------------------------------------------------------- Protected method start
	// ---------------------------------------------------------------------------- Protected method end
}
