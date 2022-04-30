package cn.hutool.db.sql;

import cn.hutool.core.collection.iter.ArrayIter;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.handler.RsHandler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * SQL执行器，全部为静态方法，执行查询或非查询的SQL语句<br>
 * 此方法为JDBC的简单封装，与数据库类型无关
 *
 * @author loolly
 */
public class SqlExecutor {

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn     数据库连接对象
	 * @param sql      SQL，使用name做为占位符，例如:name
	 * @param paramMap 参数Map
	 * @return 影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.0.10
	 */
	public static int execute(final Connection conn, final String sql, final Map<String, Object> paramMap) throws DbRuntimeException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return execute(conn, namedSql.getSql(), namedSql.getParams());
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接对象
	 * @param sql    SQL
	 * @param params 参数
	 * @return 影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static int execute(final Connection conn, final String sql, final Object... params) throws DbRuntimeException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatement(conn, sql, params);
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 执行调用存储过程<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接对象
	 * @param sql    SQL
	 * @param params 参数
	 * @return 如果执行后第一个结果是ResultSet，则返回true，否则返回false。
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static boolean call(final Connection conn, final String sql, final Object... params) throws DbRuntimeException {
		CallableStatement call = null;
		try {
			call = StatementUtil.prepareCall(conn, sql, params);
			return call.execute();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(call);
		}
	}

	/**
	 * 执行调用存储过程<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接对象
	 * @param sql    SQL
	 * @param params 参数
	 * @return ResultSet
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.1.4
	 */
	public static ResultSet callQuery(final Connection conn, final String sql, final Object... params) throws DbRuntimeException {
		try {
			return StatementUtil.prepareCall(conn, sql, params).executeQuery();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 执行非查询语句，返回主键<br>
	 * 发查询语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn     数据库连接对象
	 * @param sql      SQL
	 * @param paramMap 参数Map
	 * @return 主键
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.0.10
	 */
	public static Long executeForGeneratedKey(final Connection conn, final String sql, final Map<String, Object> paramMap) throws DbRuntimeException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return executeForGeneratedKey(conn, namedSql.getSql(), namedSql.getParams());
	}

	/**
	 * 执行非查询语句，返回主键<br>
	 * 发查询语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接对象
	 * @param sql    SQL
	 * @param params 参数
	 * @return 主键
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static Long executeForGeneratedKey(final Connection conn, final String sql, final Object... params) throws DbRuntimeException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = StatementUtil.prepareStatement(conn, sql, params);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				try {
					return rs.getLong(1);
				} catch (final SQLException e) {
					// 可能会出现没有主键返回的情况
				}
			}
			return null;
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(ps);
			DbUtil.close(rs);
		}
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn        数据库连接对象
	 * @param sql         SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static int[] executeBatch(final Connection conn, final String sql, final Iterable<Object[]> paramsBatch) throws DbRuntimeException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatementForBatch(conn, sql, paramsBatch);
			return ps.executeBatch();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn 数据库连接对象
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.5.6
	 */
	public static int[] executeBatch(final Connection conn, final String... sqls) throws DbRuntimeException {
		return executeBatch(conn, new ArrayIter<>(sqls));
	}

	/**
	 * 批量执行非查询语句<br>
	 * 语句包括 插入、更新、删除<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn 数据库连接对象
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.5.6
	 */
	public static int[] executeBatch(final Connection conn, final Iterable<String> sqls) throws DbRuntimeException {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			for (final String sql : sqls) {
				statement.addBatch(sql);
			}
			return statement.executeBatch();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(statement);
		}
	}

	/**
	 * 执行查询语句，例如：select * from table where field1=:name1 <br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>      处理结果类型
	 * @param conn     数据库连接对象
	 * @param sql      查询语句，使用参数名占位符，例如:name
	 * @param rsh      结果集处理对象
	 * @param paramMap 参数对
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.0.10
	 */
	public static <T> T query(final Connection conn, final String sql, final RsHandler<T> rsh, final Map<String, Object> paramMap) throws DbRuntimeException {
		final NamedSql namedSql = new NamedSql(sql, paramMap);
		return query(conn, namedSql.getSql(), rsh, namedSql.getParams());
	}

	/**
	 * 执行查询语句<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>        处理结果类型
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，包含参数
	 * @param rsh        结果集处理对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 * @since 5.5.3
	 */
	public static <T> T query(final Connection conn, final SqlBuilder sqlBuilder, final RsHandler<T> rsh) throws DbRuntimeException {
		return query(conn, sqlBuilder.build(), rsh, sqlBuilder.getParamValueArray());
	}

	/**
	 * 执行查询语句<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>    处理结果类型
	 * @param conn   数据库连接对象
	 * @param sql    查询语句
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static <T> T query(final Connection conn, final String sql, final RsHandler<T> rsh, final Object... params) throws DbRuntimeException {
		PreparedStatement ps = null;
		try {
			ps = StatementUtil.prepareStatement(conn, sql, params);
			return executeQuery(ps, rsh);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 执行自定义的{@link PreparedStatement}，结果使用{@link RsHandler}处理<br>
	 * 此方法主要用于自定义场景，如游标查询等
	 *
	 * @param <T>           处理结果类型
	 * @param conn          数据库连接对象
	 * @param statementFunc 自定义{@link PreparedStatement}创建函数
	 * @param rsh           自定义结果集处理
	 * @return 结果
	 * @throws DbRuntimeException SQL执行异常
	 * @since 5.7.17
	 */
	public static <T> T query(final Connection conn, final Func1<Connection, PreparedStatement> statementFunc, final RsHandler<T> rsh) throws DbRuntimeException {
		PreparedStatement ps = null;
		try {
			ps = statementFunc.callWithRuntimeException(conn);
			return executeQuery(ps, rsh);
		} finally {
			DbUtil.close(ps);
		}
	}

	// -------------------------------------------------------------------------------------- Execute With PreparedStatement

	/**
	 * 用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句，例如 CREATE TABLE 和 DROP TABLE。<br>
	 * INSERT、UPDATE 或 DELETE 语句的效果是修改表中零行或多行中的一列或多列。<br>
	 * executeUpdate 的返回值是一个整数（int），指示受影响的行数（即更新计数）。<br>
	 * 对于 CREATE TABLE 或 DROP TABLE 等不操作行的语句，executeUpdate 的返回值总为零。<br>
	 * 此方法不会关闭PreparedStatement
	 *
	 * @param ps     PreparedStatement对象
	 * @param params 参数
	 * @return 影响的行数
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static int executeUpdate(final PreparedStatement ps, final Object... params) throws DbRuntimeException {
		try {
			StatementUtil.fillParams(ps, params);
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 可用于执行任何SQL语句，返回一个boolean值，表明执行该SQL语句是否返回了ResultSet。<br>
	 * 如果执行后第一个结果是ResultSet，则返回true，否则返回false。<br>
	 * 此方法不会关闭PreparedStatement
	 *
	 * @param ps     PreparedStatement对象
	 * @param params 参数
	 * @return 如果执行后第一个结果是ResultSet，则返回true，否则返回false。
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static boolean execute(final PreparedStatement ps, final Object... params) throws DbRuntimeException {
		try {
			StatementUtil.fillParams(ps, params);
			return ps.execute();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 执行查询语句<br>
	 * 此方法不会关闭PreparedStatement
	 *
	 * @param <T>    处理结果类型
	 * @param ps     PreparedStatement
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static <T> T query(final PreparedStatement ps, final RsHandler<T> rsh, final Object... params) throws DbRuntimeException {
		try {
			StatementUtil.fillParams(ps, params);
			return executeQuery(ps, rsh);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 执行查询语句并关闭PreparedStatement
	 *
	 * @param <T>    处理结果类型
	 * @param ps     PreparedStatement
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static <T> T queryAndClosePs(final PreparedStatement ps, final RsHandler<T> rsh, final Object... params) throws DbRuntimeException {
		try {
			return query(ps, rsh, params);
		} finally {
			DbUtil.close(ps);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 执行查询
	 *
	 * @param ps  {@link PreparedStatement}
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws DbRuntimeException SQL执行异常
	 * @since 4.1.13
	 */
	private static <T> T executeQuery(final PreparedStatement ps, final RsHandler<T> rsh) throws DbRuntimeException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			DbUtil.close(rs);
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------- Private method end
}
