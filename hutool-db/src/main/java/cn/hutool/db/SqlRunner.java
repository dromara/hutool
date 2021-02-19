package cn.hutool.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.sql.Wrapper;

/**
 * SQL执行类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作<br>
 * SqlRunner中每一个方法都会打开和关闭一个链接<br>
 * 此类为线程安全的对象，可以单例使用
 * 
 * @author Luxiaolei
 * @deprecated 请使用{@link Db}
 */
@Deprecated
public class SqlRunner extends AbstractDb{
	private static final long serialVersionUID = 6626183393926198184L;

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
	 * 使用默认数据源，自动探测数据库连接池
	 * 
	 * @param group 数据源分组
	 * @return SqlRunner
	 * @since 4.0.11
	 */
	public static SqlRunner create(String group) {
		return create(DSFactory.get(group));
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
		this(ds, DialectFactory.getDialect(ds));
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名，用于识别方言
	 */
	public SqlRunner(DataSource ds, String driverClassName) {
		this(ds, DialectFactory.newDialect(driverClassName));
	}
	
	/**
	 * 构造
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public SqlRunner(DataSource ds, Dialect dialect) {
		super(ds, dialect);
	}
	//------------------------------------------------------- Constructor end

	//---------------------------------------------------------------------------- Getters and Setters start
	@Override
	public SqlRunner setWrapper(Character wrapperChar) {
		return (SqlRunner) super.setWrapper(wrapperChar);
	}
	
	@Override
	public SqlRunner setWrapper(Wrapper wrapper) {
		return (SqlRunner) super.setWrapper(wrapper);
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