package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.handler.RsHandler;

/**
 * SQL执行类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作
 * 
 * @author Luxiaolei
 * 
 */
public class SqlRunner{
	protected SqlConnRunner sqlConnRunner;
	private DataSource ds;
	
	/**
	 * 创建SqlRunner<br>
	 * 会根据数据源连接的元信息识别目标数据库类型，进而使用合适的数据源
	 * @param ds 数据源
	 * @return SqlRunner
	 */
	public static SqlRunner create(DataSource ds) {
		return ds == null ? null : new SqlRunner(ds);
	}
	
	/**
	 * 创建SqlRunner
	 * @param ds 数据源
	 * @param dialect 方言
	 * @return SqlRunner
	 */
	public static SqlRunner create(DataSource ds, Dialect dialect) {
		return new SqlRunner(ds, dialect);
	}
	
	/**
	 * 创建SqlRunner
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名
	 * @return SqlRunner
	 */
	public static SqlRunner create(DataSource ds, String driverClassName) {
		return new SqlRunner(ds, DialectFactory.newDialect(driverClassName));
	}

	//------------------------------------------------------- Constructor start
	/**
	 * 构造，从DataSource中识别方言
	 * @param ds 数据源
	 */
	public SqlRunner(DataSource ds) {
		this(ds, DialectFactory.newDialect(ds));
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public SqlRunner(DataSource ds, Dialect dialect) {
		sqlConnRunner = new SqlConnRunner(dialect);
		this.ds = ds;
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名，用于识别方言
	 */
	public SqlRunner(DataSource ds, String driverClassName) {
		sqlConnRunner = new SqlConnRunner(driverClassName);
		this.ds = ds;
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
			return SqlExecutor.query(conn, sql, rsh, params);
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
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int execute(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return SqlExecutor.execute(conn, sql, params);
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
			return SqlExecutor.executeForGeneratedKey(conn, sql, params);
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
			return SqlExecutor.executeBatch(conn, sql, paramsBatch);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	//---------------------------------------------------------------------------- CRUD start
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int insert(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.insert(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键列表
	 * @throws SQLException
	 */
	public List<Object> insertForGeneratedKeys(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.insertForGeneratedKeys(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public Long insertForGeneratedKey(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.insertForGeneratedKey(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 删除数据
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int del(Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.del(conn, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 更新数据
	 * @param record 记录
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int update(Entity record, Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.update(conn, record, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
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
			return sqlConnRunner.find(conn, fields, where, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 查询，返回所有字段
	 * 
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T find(Entity where, RsHandler<T> rsh) throws SQLException {
		return find(null, where, rsh);
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
			return sqlConnRunner.page(conn, fields, where, page, numPerPage, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 结果的条目数
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException
	 */
	public int count(Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return sqlConnRunner.count(conn, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	//---------------------------------------------------------------------------- CRUD end
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method start
}