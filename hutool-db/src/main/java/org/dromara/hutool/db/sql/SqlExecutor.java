/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.handler.RsHandler;

import java.sql.*;
import java.util.Map;

/**
 * SQL执行器，用于执行指定的SQL查询、更新语句。<br>
 * 此执行器执行原始SQL。
 *
 * @author Looly
 */
public class SqlExecutor {

	/**
	 * 创建SqlExecutor
	 *
	 * @param config 配置
	 * @param conn   {@link Connection}
	 * @return SqlExecutor
	 */
	public static SqlExecutor of(final DbConfig config, final Connection conn) {
		return new SqlExecutor(config, conn);
	}

	private final DbConfig config;
	private final Connection conn;

	/**
	 * 构造
	 *
	 * @param config 配置
	 * @param conn   {@link Connection}
	 */
	public SqlExecutor(final DbConfig config, final Connection conn) {
		this.config = config;
		this.conn = conn;
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql      SQL，使用name做为占位符，例如:name
	 * @param paramMap 参数Map
	 * @return 影响的行数
	 * @throws DbException SQL执行异常
	 * @since 4.0.10
	 */
	public int execute(final String sql, final Map<String, Object> paramMap) throws DbException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return execute(namedSql.getSql(), namedSql.getParamArray());
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return 影响的行数
	 * @throws DbException SQL执行异常
	 */
	public int execute(final String sql, final Object... params) throws DbException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatement(false, this.config, this.conn, sql, params);
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 执行调用存储过程<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return 如果执行后第一个结果是ResultSet，则返回true，否则返回false。
	 * @throws DbException SQL执行异常
	 */
	public boolean call(final String sql, final Object... params) throws DbException {
		CallableStatement call = null;
		try {
			call = StatementUtil.prepareCall(this.config, this.conn, sql, params);
			return call.execute();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(call);
		}
	}

	/**
	 * 执行调用存储过程<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return ResultSet
	 * @throws DbException SQL执行异常
	 * @since 4.1.4
	 */
	public ResultSet callQuery(final String sql, final Object... params) throws DbException {
		try {
			return StatementUtil.prepareCall(this.config, this.conn, sql, params).executeQuery();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 执行非查询语句，返回主键<br>
	 * 发查询语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql      SQL
	 * @param paramMap 参数Map
	 * @return 主键
	 * @throws DbException SQL执行异常
	 * @since 4.0.10
	 */
	public Long executeForGeneratedKey(final String sql, final Map<String, Object> paramMap) throws DbException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return executeForGeneratedKey(namedSql.getSql(), namedSql.getParamArray());
	}

	/**
	 * 执行非查询语句，返回主键<br>
	 * 发查询语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return 主键
	 * @throws DbException SQL执行异常
	 */
	public Long executeForGeneratedKey(final String sql, final Object... params) throws DbException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatement(true, this.config, this.conn, sql, params);
			ps.executeUpdate();
			return StatementUtil.getGeneratedKeyOfLong(ps);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sql         SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 */
	public int[] executeBatch(final String sql, final Iterable<Object[]> paramsBatch) throws DbException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatementForBatch(this.config, this.conn, sql, paramsBatch);
			return ps.executeBatch();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 * @since 4.5.6
	 */
	public int[] executeBatch(final String... sqls) throws DbException {
		return executeBatch(new ArrayIter<>(sqls));
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 * @since 4.5.6
	 */
	public int[] executeBatch(final Iterable<String> sqls) throws DbException {
		Statement statement = null;
		try {
			statement = this.conn.createStatement();
			for (final String sql : sqls) {
				statement.addBatch(sql);
			}
			return statement.executeBatch();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(statement);
		}
	}

	// region ----- query

	/**
	 * 执行查询语句，例如：select * from table where field1=:name1 <br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>      处理结果类型
	 * @param sql      查询语句，使用参数名占位符，例如:name
	 * @param rsh      结果集处理对象
	 * @param paramMap 参数对
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 4.0.10
	 */
	public <T> T query(final String sql, final RsHandler<T> rsh, final Map<String, Object> paramMap) throws DbException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return query(namedSql.getSql(), rsh, namedSql.getParamArray());
	}

	/**
	 * 执行查询语句<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>        处理结果类型
	 * @param sqlBuilder SQL构建器，包含参数
	 * @param rsh        结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 5.5.3
	 */
	public <T> T query(final SqlBuilder sqlBuilder, final RsHandler<T> rsh) throws DbException {
		return query(sqlBuilder.build(), rsh, sqlBuilder.getParamValueArray());
	}

	/**
	 * 执行查询语句<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>    处理结果类型
	 * @param sql    查询语句
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T query(final String sql, final RsHandler<T> rsh, final Object... params) throws DbException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatement(false, this.config, this.conn, sql, params);
			return StatementUtil.executeQuery(ps, rsh);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}

	/**
	 * 执行自定义的{@link PreparedStatement}，结果使用{@link RsHandler}处理<br>
	 * 此方法主要用于自定义场景，如游标查询等
	 *
	 * @param <T>           处理结果类型
	 * @param statementFunc 自定义{@link PreparedStatement}创建函数
	 * @param rsh           自定义结果集处理
	 * @return 结果
	 * @throws DbException SQL执行异常
	 * @since 5.7.17
	 */
	public <T> T query(final SerFunction<Connection, PreparedStatement> statementFunc, final RsHandler<T> rsh) throws DbException {
		PreparedStatement ps = null;
		try {
			ps = statementFunc.apply(conn);
			return StatementUtil.executeQuery(ps, rsh);
		} finally {
			IoUtil.closeQuietly(ps);
		}
	}
	// endregion
}
