package cn.hutool.db;

import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.sql.Wrapper;
import cn.hutool.db.transaction.TransactionLevel;
import cn.hutool.log.StaticLog;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库操作类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作<br>
 *
 * @author Looly
 * @since 4.1.2
 */
public class Db extends AbstractDb {
	private static final long serialVersionUID = -3378415769645309514L;

	/**
	 * 创建Db<br>
	 * 使用默认数据源，自动探测数据库连接池
	 *
	 * @return Db
	 */
	public static Db use() {
		return use(DSFactory.get());
	}

	/**
	 * 创建Db<br>
	 * 使用默认数据源，自动探测数据库连接池
	 *
	 * @param group 数据源分组
	 * @return Db
	 */
	public static Db use(String group) {
		return use(DSFactory.get(group));
	}

	/**
	 * 创建Db<br>
	 * 会根据数据源连接的元信息识别目标数据库类型，进而使用合适的数据源
	 *
	 * @param ds 数据源
	 * @return Db
	 */
	public static Db use(DataSource ds) {
		return ds == null ? null : new Db(ds);
	}

	/**
	 * 创建Db
	 *
	 * @param ds 数据源
	 * @param dialect 方言
	 * @return Db
	 */
	public static Db use(DataSource ds, Dialect dialect) {
		return new Db(ds, dialect);
	}

	/**
	 * 创建Db
	 *
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名
	 * @return Db
	 */
	public static Db use(DataSource ds, String driverClassName) {
		return new Db(ds, DialectFactory.newDialect(driverClassName));
	}

	// ---------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，从DataSource中识别方言
	 *
	 * @param ds 数据源
	 */
	public Db(DataSource ds) {
		this(ds, DialectFactory.getDialect(ds));
	}

	/**
	 * 构造
	 *
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名，用于识别方言
	 */
	public Db(DataSource ds, String driverClassName) {
		this(ds, DialectFactory.newDialect(driverClassName));
	}

	/**
	 * 构造
	 *
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public Db(DataSource ds, Dialect dialect) {
		super(ds, dialect);
	}
	// ---------------------------------------------------------------------------- Constructor end

	// ---------------------------------------------------------------------------- Getters and Setters start
	@Override
	public Db setWrapper(Character wrapperChar) {
		return (Db) super.setWrapper(wrapperChar);
	}

	@Override
	public Db setWrapper(Wrapper wrapper) {
		return (Db) super.setWrapper(wrapper);
	}

	@Override
	public Db disableWrapper() {
		return (Db)super.disableWrapper();
	}
	// ---------------------------------------------------------------------------- Getters and Setters end

	@Override
	public Connection getConnection() throws SQLException {
		return ThreadLocalConnection.INSTANCE.get(this.ds);
	}

	@Override
	public void closeConnection(Connection conn) {
		try {
			if (conn != null && false == conn.getAutoCommit()) {
				// 事务中的Session忽略关闭事件
				return;
			}
		} catch (SQLException e) {
			// ignore
		}

		ThreadLocalConnection.INSTANCE.close(this.ds);
	}

	/**
	 * 执行事务，使用默认的事务级别<br>
	 * 在同一事务中，所有对数据库操作都是原子的，同时提交或者同时回滚
	 *
	 * @param func 事务函数，所有操作应在同一函数下执行，确保在同一事务中
	 * @return this
	 * @throws SQLException SQL异常
	 */
	public Db tx(VoidFunc1<Db> func) throws SQLException {
		return tx(null, func);
	}

	/**
	 * 执行事务<br>
	 * 在同一事务中，所有对数据库操作都是原子的，同时提交或者同时回滚
	 *
	 * @param transactionLevel 事务级别枚举，null表示使用JDBC默认事务
	 * @param func 事务函数，所有操作应在同一函数下执行，确保在同一事务中
	 * @return this
	 * @throws SQLException SQL异常
	 */
	public Db tx(TransactionLevel transactionLevel, VoidFunc1<Db> func) throws SQLException {
		final Connection conn = getConnection();

		// 检查是否支持事务
		checkTransactionSupported(conn);

		// 设置事务级别
		if (null != transactionLevel) {
			final int level = transactionLevel.getLevel();
			if (conn.getTransactionIsolation() < level) {
				// 用户定义的事务级别如果比默认级别更严格，则按照严格的级别进行
				//noinspection MagicConstant
				conn.setTransactionIsolation(level);
			}
		}

		// 开始事务
		boolean autoCommit = conn.getAutoCommit();
		if (autoCommit) {
			conn.setAutoCommit(false);
		}

		// 执行事务
		try {
			func.call(this);
			// 提交
			conn.commit();
		} catch (Throwable e) {
			quietRollback(conn);
			throw (e instanceof SQLException) ? (SQLException) e : new SQLException(e);
		} finally {
			// 还原事务状态
			quietSetAutoCommit(conn, autoCommit);
			// 关闭连接或将连接归还连接池
			closeConnection(conn);
		}

		return this;
	}

	// ---------------------------------------------------------------------------- Private method start
	/**
	 * 静默回滚事务
	 *
	 * @param conn Connection
	 */
	private void quietRollback(Connection conn) {
		if (null != conn) {
			try {
				conn.rollback();
			} catch (Exception e) {
				StaticLog.error(e);
			}
		}
	}

	/**
	 * 静默设置自动提交
	 *
	 * @param conn Connection
	 * @param autoCommit 是否自动提交
	 */
	private void quietSetAutoCommit(Connection conn, Boolean autoCommit) {
		if (null != conn && null != autoCommit) {
			try {
				conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
				StaticLog.error(e);
			}
		}
	}
	// ---------------------------------------------------------------------------- Private method end
}
