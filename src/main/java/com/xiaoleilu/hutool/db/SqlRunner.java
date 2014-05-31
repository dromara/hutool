package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.db.dialect.impl.AnsiSqlDialect;
import com.xiaoleilu.hutool.db.handler.NumberHandler;

/**
 * SQL执行类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作
 * 
 * @author Luxiaolei
 * 
 */
public class SqlRunner extends SqlExecutor{
	private DataSource ds;
	private Dialect dialect;

	//------------------------------------------------------- Constructor start
	/**
	 * 构造，使用ANSI的SQL方言
	 * @param ds 数据源
	 */
	public SqlRunner(DataSource ds) {
		this.ds = ds;
		//默认使用ANSI的SQL方言
		dialect = new AnsiSqlDialect();
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public SqlRunner(DataSource ds, Dialect dialect) {
		this.ds = ds;
		this.dialect = dialect;
	}
	//------------------------------------------------------- Constructor end

	/**
	 * 查询
	 * 
	 * @param sql 查询语句
	 * @param rsh 结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T query(String sql, RsHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return query(conn, sql, rsh, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 * 
	 * @param sql SQL
	 * @param params 参数
	 * @return 主键
	 * @throws SQLException
	 */
	public int execute(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return execute(conn, sql, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 * 
	 * @param sql SQL
	 * @param params 参数
	 * @return 主键
	 * @throws SQLException
	 */
	public Long executeForGeneratedKey(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return executeForGeneratedKey(conn, sql, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 批量执行非查询语句
	 * 
	 * @param sql SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws SQLException
	 */
	public int[] executeBatch(String sql, Object[]... paramsBatch) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return executeBatch(conn, sql, paramsBatch);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	//---------------------------------------------------------------------------- CRUD start
	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public Long insert(Connection conn, Entity record) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			return DbUtil.getGeneratedKey(ps);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public Long insert(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return insert(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 删除数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param where 条件
	 * @return 主键
	 * @throws SQLException
	 */
	public int del(Connection conn, Entity where) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, where);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 删除数据
	 * @param where 条件
	 * @return 主键
	 * @throws SQLException
	 */
	public int del(Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return del(conn, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 更新数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public int update(Connection conn, Entity record, Entity where) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForUpdate(conn, record, where);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 更新数据
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public int update(Entity record, Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return update(conn, record, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 查询<br>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T find(Connection conn, Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForFind(conn, fields, where);
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	
	/**
	 * 查询
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T find(Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return find(conn, fields, where, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T page(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForPage(conn, fields, where, page, numPerPage);
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	
	/**
	 * 分页查询<br/>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T page(Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return page(fields, where, page, numPerPage, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 结果的条目数
	 * @param conn 数据库连接对象
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException
	 */
	public int count(Connection conn, Entity where) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForCount(conn, where);
			rs = ps.executeQuery();
			return new NumberHandler().handle(rs).intValue();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	//---------------------------------------------------------------------------- CRUD end
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method start
}