package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.ds.DSFactory;

/**
 * SQL执行类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作<br>
 * SqlRunner中每一个方法都会打开和关闭一个链接<br>
 * 此类为线程安全的对象，可以单例使用
 * 
 * @author Luxiaolei
 * 
 */
public class SqlRunner extends AbstractSqlRunner{
	private DataSource ds;
	
	/**
	 * 创建SqlRunner<br>
	 * 使用默认数据源，自动探测数据库连接池
	 * @return SqlRunner
	 * @since 3.0.6
	 */
	public static SqlRunner create() {
		return create(DSFactory.get());
	}
	
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
		this.runner = new SqlConnRunner(dialect);
		this.ds = ds;
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名，用于识别方言
	 */
	public SqlRunner(DataSource ds, String driverClassName) {
		this.runner = new SqlConnRunner(driverClassName);
		this.ds = ds;
	}
	//------------------------------------------------------- Constructor end

	//---------------------------------------------------------------------------- Getters and Setters start
	public SqlConnRunner getRunner() {
		return this.runner;
	}
	public void setRunner(SqlConnRunner runner) {
		this.runner = runner;
	}
	//---------------------------------------------------------------------------- Getters and Setters end
	
	@Override
	public Connection getConnection() throws SQLException{
		return ds.getConnection();
	}

	@Override
	public void closeConnection(Connection conn) {
		DbUtil.close(conn);
	}
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method end
}