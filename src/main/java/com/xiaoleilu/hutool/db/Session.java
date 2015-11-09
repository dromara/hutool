package com.xiaoleilu.hutool.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.handler.RsHandler;
import com.xiaoleilu.hutool.db.sql.SqlExecutor;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 数据库SQL执行会话<br>
 * 一个会话只维护一个连接，推荐在执行完后关闭Session，避免重用<br>
 * 本对象并不是线程安全的，多个线程共用一个Session将会导致不可预知的问题
 * @author loolly
 *
 */
public class Session implements Closeable{
	private final static Log log = StaticLog.get();
	
	private Connection conn = null;
	private SqlConnRunner runner = null;
	
	private Boolean isSupportTransaction = null;
	
	/**
	 * 创建会话
	 * @param ds 数据源
	 */
	public static Session create(DataSource ds) {
		return new Session(ds);
	}
	
	/**
	 * 创建会话
	 * @param conn 数据库连接对象
	 */
	public static Session create(Connection conn) {
		return new Session(conn);
	}

	//---------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param ds 数据源
	 */
	public Session(DataSource ds) {
		try {
			this.conn = ds.getConnection();
		} catch (SQLException e) {
			throw new DbRuntimeException("Get connection error!", e);
		}
		this.runner = new SqlConnRunner(DialectFactory.newDialect(conn));
	}
	
	/**
	 * 构造
	 * @param conn 数据库连接对象
	 */
	public Session(Connection conn) {
		this.conn = conn;
		this.runner = new SqlConnRunner(DialectFactory.newDialect(conn));
	}
	
	//---------------------------------------------------------------------------- Constructor end
	
	//---------------------------------------------------------------------------- Getters and Setters end
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public SqlConnRunner getRunner() {
		return runner;
	}
	public void setRunner(SqlConnRunner runner) {
		this.runner = runner;
	}
	//---------------------------------------------------------------------------- Getters and Setters end
	
	//---------------------------------------------------------------------------- Transaction method start
	/**
	 * 开始事务
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException {
		if(null == isSupportTransaction) {
			isSupportTransaction = conn.getMetaData().supportsTransactions();
		}else if(false == isSupportTransaction) {
			throw new SQLException("Transaction not supported for current database!");
		}
		conn.setAutoCommit(false);
	}
	
	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public void commit() throws SQLException{
		try {
			conn.commit();
		} catch (SQLException e) {
			throw e;
		}finally {
			conn.setAutoCommit(true);			//事务结束，恢复自动提交
		}
	}
	
	/**
	 * 回滚事务
	 * @throws Exception
	 */
	public void rollback() throws SQLException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw e;
		}finally {
			try {
				conn.setAutoCommit(true);	//事务结束，恢复自动提交
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	/**
	 * 静默回滚事务<br>
	 * 回滚事务
	 * @throws SQLException
	 */
	public void quietRollback() {
		try {
			conn.rollback();
		} catch (Exception e) {
			log.error(e);
		}finally {
			try {
				conn.setAutoCommit(true);	//事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * 回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 * @param savepoint 保存点
	 * @throws SQLException
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		try {
			conn.rollback(savepoint);
		} catch (SQLException e) {
			throw e;
		}finally {
			try {
				conn.setAutoCommit(true);	//事务结束，恢复自动提交
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	/**
	 * 静默回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 * @param savepoint 保存点
	 * @throws SQLException
	 */
	public void quietRollback(Savepoint savepoint) throws SQLException {
		try {
			conn.rollback(savepoint);
		} catch (Exception e) {
			log.error(e);
		}finally {
			try {
				conn.setAutoCommit(true);	//事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * 设置保存点
	 * @return 保存点对象
	 * @throws SQLException
	 */
	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}
	
	/**
	 * 设置保存点
	 * @param name 保存点的名称
	 * @return 保存点对象
	 * @throws SQLException
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}
	
	/**
	 * 设置事务的隔离级别<br>
	 * 
	 * Connection.TRANSACTION_NONE                             驱动不支持事务<br>
	 * Connection.TRANSACTION_READ_UNCOMMITTED   允许脏读、不可重复读和幻读<br>
	 * Connection.TRANSACTION_READ_COMMITTED        禁止脏读，但允许不可重复读和幻读<br>
	 * Connection.TRANSACTION_REPEATABLE_READ         禁止脏读和不可重复读，单运行幻读<br>
	 * Connection.TRANSACTION_SERIALIZABLE                 禁止脏读、不可重复读和幻读<br>
	 * 
	 * @param level 隔离级别
	 * @throws SQLException
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		if(conn.getMetaData().supportsTransactionIsolationLevel(level) == false) {
			throw new SQLException(StrUtil.format("Transaction isolation [{}] not support!", level));
		}
		conn.setTransactionIsolation(level);
	}
	//---------------------------------------------------------------------------- Transaction method end
	
	//---------------------------------------------------------------------------- Query and Execute start
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
		return SqlExecutor.query(conn, sql, rsh, params);
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
		return SqlExecutor.execute(conn, sql, params);
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
		return SqlExecutor.executeForGeneratedKey(conn, sql, params);
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
		return SqlExecutor.executeBatch(conn, sql, paramsBatch);
	}
	//---------------------------------------------------------------------------- Query and Execute end

	//---------------------------------------------------------------------------- CRUD start
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int insert(Entity record) throws SQLException {
		return runner.insert(conn, record);
	}
	
	/**
	 * 批量插入数据
	 * @param records 记录列表
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int[] insert(Collection<Entity> records) throws SQLException {
		return runner.insert(conn, records);
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键列表
	 * @throws SQLException
	 */
	public List<Object> insertForGeneratedKeys(Entity record) throws SQLException {
		return runner.insertForGeneratedKeys(conn, record);
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 数字主键
	 * @throws SQLException
	 */
	public Long insertForGeneratedKey(Entity record) throws SQLException {
		return runner.insertForGeneratedKey(conn, record);
	}
	
	/**
	 * 删除数据
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int del(Entity where) throws SQLException {
		return runner.del(conn, where);
	}
	
	/**
	 * 更新数据
	 * @param record 记录
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int update(Entity record, Entity where) throws SQLException {
		return runner.update(conn, record, where);
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
		return runner.find(conn, fields, where, rsh);
	}
	
	/**
	 * 结果的条目数
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException
	 */
	public int count(Entity where) throws SQLException {
		return runner.count(conn, where);
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
		return runner.page(conn, fields, where, page, numPerPage, rsh);
	}
	
	/**
	 * 分页查询<br/>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 分页对象
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T page(Collection<String> fields, Entity where, Page page, RsHandler<T> rsh) throws SQLException {
		return runner.page(conn, fields, where, page, rsh);
	}
	
	/**
	 * 分页查询<br/>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @return 结果对象
	 * @throws SQLException
	 */
	public PageResult<Entity> page(Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		return runner.page(conn, fields, where, page, numPerPage);
	}
	
	/**
	 * 分页查询<br/>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @return 结果对象
	 * @throws SQLException
	 */
	public PageResult<Entity> page(Collection<String> fields, Entity where, Page page) throws SQLException {
		return runner.page(conn, fields, where, page);
	}
	
	/**
	 * 分页查询<br/>
	 * 
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @return 结果对象
	 * @throws SQLException
	 */
	public PageResult<Entity> page(Entity where, Page page) throws SQLException {
		return runner.page(conn, where, page);
	}
	//---------------------------------------------------------------------------- CRUD end
	
	@Override
	public void close() {
		DbUtil.close(conn);
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
