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

package org.dromara.hutool.db;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.handler.NumberHandler;
import org.dromara.hutool.db.handler.PageResultHandler;
import org.dromara.hutool.db.handler.RsHandler;
import org.dromara.hutool.db.sql.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供基于方言的原始增删改查执行封装
 *
 * @author looly
 * @since 5.5.3
 */
public class DialectRunner implements Serializable {
	private static final long serialVersionUID = 1L;

	private final DbConfig config;
	private final Dialect dialect;

	/**
	 * 构造
	 *
	 * @param config  数据库配置
	 * @param dialect 方言
	 */
	public DialectRunner(final DbConfig config, final Dialect dialect) {
		this.config = config;
		this.dialect = dialect;
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
	 * @throws DbException SQL执行异常
	 */
	public int[] insert(final Connection conn, final Entity... records) throws DbException {
		checkConn(conn);
		if (ArrayUtil.isEmpty(records)) {
			return new int[]{0};
		}

		PreparedStatement ps = null;
		try {
			if (1 == records.length) {
				//单条单独处理
				ps = dialect.psForInsert(false, conn, records[0]);
				return new int[]{ps.executeUpdate()};
			}

			// 批量
			ps = dialect.psForInsertBatch(conn, records);
			return ps.executeBatch();
		} catch (final SQLException e) {
			throw new DbException(e);
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
	 * @throws DbException SQL执行异常
	 * @since 5.7.20
	 */
	public int upsert(final Connection conn, final Entity record, final String... keys) throws DbException {
		PreparedStatement ps = this.dialect.psForUpsert(conn, record, keys);
		try {
			ps = this.dialect.psForUpsert(conn, record, keys);
		} catch (final DbException ignore) {
			// 方言不支持，使用默认
		}
		if (null != ps) {
			try {
				return ps.executeUpdate();
			} catch (final SQLException e) {
				throw new DbException(e);
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
	 * @throws DbException SQL执行异常
	 */
	public int insertOrUpdate(final Connection conn, final Entity record, final String... keys) throws DbException {
		final Entity where = record.filterNew(keys);
		if (MapUtil.isNotEmpty(where) && count(conn, Query.of(where)) > 0) {
			return update(conn, record.removeNew(keys), where);
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
	 * @throws DbException SQL执行异常
	 */
	public <T> T insert(final Connection conn, final Entity record, final RsHandler<T> generatedKeysHandler) throws DbException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new DbException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(true, conn, record);
			ps.executeUpdate();
			if (null == generatedKeysHandler) {
				return null;
			}
			return StatementUtil.getGeneratedKeys(ps, generatedKeysHandler);
		} catch (final SQLException e) {
			throw new DbException(e);
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
	 * @throws DbException SQL执行异常
	 */
	public int del(final Connection conn, final Entity where) throws DbException {
		checkConn(conn);
		if (MapUtil.isEmpty(where)) {
			//不允许做全表删除
			throw new DbException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, Query.of(where));
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbException(e);
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
	 * @throws DbException SQL执行异常
	 */
	public int update(final Connection conn, final Entity record, final Entity where) throws DbException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new DbException("Empty entity provided!");
		}
		if (MapUtil.isEmpty(where)) {
			//不允许做全表更新
			throw new DbException("Empty where provided!");
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
			throw new DbException(e);
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
	 * @throws DbException SQL执行异常
	 */
	public <T> T find(final Connection conn, final Query query, final RsHandler<T> rsh) throws DbException {
		checkConn(conn);
		Assert.notNull(query, "[query] is null !");
		return StatementUtil.executeQuery(dialect.psForFind(conn, query), rsh);
	}

	/**
	 * 获取结果总数，生成类似于select count(1) from XXX wher XXX=? and YYY=?
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询
	 * @return 复合条件的结果数
	 * @throws DbException SQL执行异常
	 */
	public long count(final Connection conn, final Query query) throws DbException {
		checkConn(conn);
		return StatementUtil.executeQuery(dialect.psForCount(conn, query), NumberHandler.INSTANCE).longValue();
	}

	/**
	 * 获取查询结果总数，生成类似于 SELECT count(1) from (sql) hutool_alias_count_<br>
	 * 此方法会重新构建{@link SqlBuilder}，并去除末尾的order by子句
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder 查询语句
	 * @return 复合条件的结果数
	 * @throws DbException SQL执行异常
	 * @since 5.7.2
	 */
	public long count(final Connection conn, final SqlBuilder sqlBuilder) throws DbException {
		checkConn(conn);
		String selectSql = sqlBuilder.build();

		// 去除order by 子句
		final Pattern pattern = PatternPool.get("(.*?)[\\s]order[\\s]by[\\s][^\\s]+\\s(asc|desc)?", Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(selectSql);
		if (matcher.matches()) {
			selectSql = matcher.group(1);
		}

		return StatementUtil.executeQuery(dialect.psForCount(conn,
			SqlBuilder.of(selectSql).addParams(sqlBuilder.getParamValueArray())), NumberHandler.INSTANCE).longValue();
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Connection conn, final Query query) throws DbException {
		final Page page = query.getPage();
		final PageResultHandler<Entity> entityResultHandler = PageResultHandler.of(
			// 分页查询中总数的查询要去掉分页信息
			new PageResult<>(page, (int) count(conn, query.clone().setPage(null))));

		return page(conn, query, entityResultHandler.setCaseInsensitive(this.config.isCaseInsensitive()));
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
	 * @throws DbException SQL执行异常
	 */
	public <T> T page(final Connection conn, final Query query, final RsHandler<T> rsh) throws DbException {
		checkConn(conn);
		if (null == query.getPage()) {
			return this.find(conn, query, rsh);
		}

		return StatementUtil.executeQuery(dialect.psForPage(conn, query), rsh);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Connection conn, final SqlBuilder sqlBuilder, final Page page) throws DbException {
		final PageResultHandler<Entity> entityResultHandler = PageResultHandler.of(
			new PageResult<>(page, (int) count(conn, sqlBuilder)));

		return page(conn, sqlBuilder, page, entityResultHandler.setCaseInsensitive(this.config.isCaseInsensitive()));
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
	 * @throws DbException SQL执行异常
	 * @since 5.5.3
	 */
	public <T> T page(final Connection conn, final SqlBuilder sqlBuilder, final Page page, final RsHandler<T> rsh) throws DbException {
		checkConn(conn);
		return StatementUtil.executeQuery(dialect.psForPage(conn, sqlBuilder, page), rsh);
	}
	//---------------------------------------------------------------------------- CRUD end

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

	/**
	 * 检查{@link Connection} 可用性
	 * @param conn 数据库连接
	 */
	private void checkConn(final Connection conn) {
		Assert.notNull(conn, "Connection object must be not null!");
	}
}
