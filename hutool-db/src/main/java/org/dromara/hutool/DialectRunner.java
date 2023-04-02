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

package org.dromara.hutool;

import org.dromara.hutool.dialect.Dialect;
import org.dromara.hutool.handler.PageResultHandler;
import org.dromara.hutool.handler.RsHandler;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.sql.SqlExecutor;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.dialect.DialectFactory;
import org.dromara.hutool.handler.NumberHandler;
import org.dromara.hutool.sql.Query;
import org.dromara.hutool.sql.SqlBuilder;
import org.dromara.hutool.sql.SqlUtil;
import org.dromara.hutool.sql.QuoteWrapper;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 提供基于方言的原始增删改查执行封装
 *
 * @author looly
 * @since 5.5.3
 */
public class DialectRunner implements Serializable {
	private static final long serialVersionUID = 1L;

	private Dialect dialect;
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected boolean caseInsensitive = GlobalDbConfig.caseInsensitive;

	/**
	 * 构造
	 *
	 * @param dialect 方言
	 */
	public DialectRunner(final Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 构造
	 *
	 * @param driverClassName 驱动类名，用于识别方言
	 */
	public DialectRunner(final String driverClassName) {
		this(DialectFactory.newDialect(driverClassName));
	}

	//---------------------------------------------------------------------------- CRUD start

	/**
	 * 批量插入数据<br>
	 * 批量插入必须严格保持Entity的结构一致，不一致会导致插入数据出现不可预知的结果<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn    数据库连接
	 * @param records 记录列表，记录KV必须严格一致
	 * @return 插入行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public int[] insert(final Connection conn, final Entity... records) throws DbRuntimeException {
		checkConn(conn);
		if (ArrayUtil.isEmpty(records)) {
			return new int[]{0};
		}

		PreparedStatement ps = null;
		try {
			if (1 == records.length) {
				//单条单独处理
				ps = dialect.psForInsert(conn, records[0]);
				return new int[]{ps.executeUpdate()};
			}

			// 批量
			ps = dialect.psForInsertBatch(conn, records);
			return ps.executeBatch();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 更新或插入数据<br>
	 * 此方法不会关闭Connection
	 * 如果方言未实现此方法则内部自动使用insertOrUpdate来替代功能
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @param keys   需要检查唯一性的字段
	 * @return 插入行数
	 * @throws DbRuntimeException SQL执行异常
	 * @since 5.7.20
	 */
	public int upsert(final Connection conn, final Entity record, final String... keys) throws DbRuntimeException {
		PreparedStatement ps = null;
		try {
			ps = getDialect().psForUpsert(conn, record, keys);
		} catch (final SQLException ignore) {
			// 方言不支持，使用默认
		}
		if (null != ps) {
			try {
				return ps.executeUpdate();
			} catch (final SQLException e) {
				throw new DbRuntimeException(e);
			} finally {
				IoUtil.closeQuietly(ps);
			}
		} else {
			return insertOrUpdate(conn, record, keys);
		}
	}

	/**
	 * 插入或更新数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @param keys   需要检查唯一性的字段
	 * @return 插入行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public int insertOrUpdate(final Connection conn, final Entity record, final String... keys) throws DbRuntimeException {
		final Entity where = record.filter(keys);
		if (MapUtil.isNotEmpty(where) && count(conn, Query.of(where)) > 0) {
			return update(conn, record, where);
		} else {
			return insert(conn, record)[0];
		}
	}

	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>                  主键类型，可能为数字或对象列表
	 * @param conn                 数据库连接
	 * @param record               记录
	 * @param generatedKeysHandler 自增主键处理器，用于定义返回自增主键的范围和类型
	 * @return 主键列表
	 * @throws DbRuntimeException SQL执行异常
	 */
	public <T> T insert(final Connection conn, final Entity record, final RsHandler<T> generatedKeysHandler) throws DbRuntimeException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new DbRuntimeException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			if (null == generatedKeysHandler) {
				return null;
			}
			return StatementUtil.getGeneratedKeys(ps, generatedKeysHandler);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 删除数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn  数据库连接
	 * @param where 条件
	 * @return 影响行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public int del(final Connection conn, final Entity where) throws DbRuntimeException {
		checkConn(conn);
		if (MapUtil.isEmpty(where)) {
			//不允许做全表删除
			throw new DbRuntimeException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, Query.of(where));
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 更新数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @param where  条件
	 * @return 影响行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public int update(final Connection conn, final Entity record, final Entity where) throws DbRuntimeException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new DbRuntimeException("Empty entity provided!");
		}
		if (MapUtil.isEmpty(where)) {
			//不允许做全表更新
			throw new DbRuntimeException("Empty where provided!");
		}

		//表名可以从被更新记录的Entity中获得，也可以从Where中获得
		String tableName = record.getTableName();
		if (StrUtil.isBlank(tableName)) {
			tableName = where.getTableName();
			record.setTableName(tableName);
		}

		final Query query = new Query(SqlUtil.buildConditions(where), tableName);
		PreparedStatement ps = null;
		try {
			ps = dialect.psForUpdate(conn, record, query);
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>   结果对象类型
	 * @param conn  数据库连接对象
	 * @param query {@link Query}
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public <T> T find(final Connection conn, final Query query, final RsHandler<T> rsh) throws DbRuntimeException {
		checkConn(conn);
		Assert.notNull(query, "[query] is null !");
		try {
			return SqlExecutor.queryAndClosePs(dialect.psForFind(conn, query), rsh);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 获取结果总数，生成类似于select count(1) from XXX wher XXX=? and YYY=?
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询
	 * @return 复合条件的结果数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public long count(final Connection conn, final Query query) throws DbRuntimeException {
		checkConn(conn);
		try {
			return SqlExecutor.queryAndClosePs(dialect.psForCount(conn, query), new NumberHandler()).longValue();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 获取查询结果总数，生成类似于 SELECT count(1) from (sql) hutool_alias_count_<br>
	 * 此方法会重新构建{@link SqlBuilder}，并去除末尾的order by子句
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder 查询语句
	 * @return 复合条件的结果数
	 * @throws DbRuntimeException SQL执行异常
	 * @since 5.7.2
	 */
	public long count(final Connection conn, final SqlBuilder sqlBuilder) throws DbRuntimeException {
		checkConn(conn);

		String selectSql = sqlBuilder.build();
		// 去除最后的order by 子句
		final int orderByIndex = StrUtil.lastIndexOfIgnoreCase(selectSql, " order by");
		if (orderByIndex > 0) {
			selectSql = StrUtil.subPre(selectSql, orderByIndex);
		}
		try {
			return SqlExecutor.queryAndClosePs(dialect.psForCount(conn,
							SqlBuilder.of(selectSql).addParams(sqlBuilder.getParamValueArray())),
					new NumberHandler()).longValue();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public PageResult<Entity> page(final Connection conn, final Query query) throws DbRuntimeException {
		final Page page = query.getPage();
		final PageResultHandler pageResultHandler = new PageResultHandler(
				new PageResult<>(page.getPageNumber(), page.getPageSize(),
						// 分页查询中总数的查询要去掉分页信息
						(int) count(conn, query.clone().setPage(null))),
				this.caseInsensitive);
		return page(conn, query, pageResultHandler);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>   结果对象类型
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public <T> T page(final Connection conn, final Query query, final RsHandler<T> rsh) throws DbRuntimeException {
		checkConn(conn);
		if (null == query.getPage()) {
			return this.find(conn, query, rsh);
		}

		try {
			return SqlExecutor.queryAndClosePs(dialect.psForPage(conn, query), rsh);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public PageResult<Entity> page(final Connection conn, final SqlBuilder sqlBuilder, final Page page) throws DbRuntimeException {
		final PageResultHandler pageResultHandler = new PageResultHandler(
				new PageResult<>(page.getPageNumber(), page.getPageSize(), (int) count(conn, sqlBuilder)),
				this.caseInsensitive);
		return page(conn, sqlBuilder, page, pageResultHandler);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>        结果对象类型
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @param rsh        结果集处理对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 * @since 5.5.3
	 */
	public <T> T page(final Connection conn, final SqlBuilder sqlBuilder, final Page page, final RsHandler<T> rsh) throws DbRuntimeException {
		checkConn(conn);
		if (null == page) {
			return SqlExecutor.query(conn, sqlBuilder, rsh);
		}

		final PreparedStatement ps;
		try {
			ps = dialect.psForPage(conn, sqlBuilder, page);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
		return SqlExecutor.queryAndClosePs(ps, rsh);
	}
	//---------------------------------------------------------------------------- CRUD end

	//---------------------------------------------------------------------------- Getters and Setters start

	/**
	 * 设置是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param caseInsensitive 否在结果中忽略大小写
	 * @since 5.2.4
	 */
	public void setCaseInsensitive(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * @return SQL方言
	 */
	public Dialect getDialect() {
		return dialect;
	}

	/**
	 * 设置SQL方言
	 *
	 * @param dialect 方言
	 */
	public void setDialect(final Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapperChar 包装字符，字符会在SQL生成时位于表名和字段名两边，null时表示取消包装
	 */
	public void setWrapper(final Character wrapperChar) {
		setWrapper(new QuoteWrapper(wrapperChar));
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param quoteWrapper 包装器，null表示取消包装
	 */
	public void setWrapper(final QuoteWrapper quoteWrapper) {
		this.dialect.setWrapper(quoteWrapper);
	}
	//---------------------------------------------------------------------------- Getters and Setters end

	//---------------------------------------------------------------------------- Private method start
	private void checkConn(final Connection conn) {
		Assert.notNull(conn, "Connection object must be not null!");
	}
	//---------------------------------------------------------------------------- Private method start
}
