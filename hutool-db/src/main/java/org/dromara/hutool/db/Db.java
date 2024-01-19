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

import org.dromara.hutool.core.func.SerConsumer;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.dialect.DialectFactory;
import org.dromara.hutool.db.ds.DSUtil;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.db.transaction.TransactionLevel;
import org.dromara.hutool.log.LogUtil;

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
public class Db extends AbstractDb<Db> {
	private static final long serialVersionUID = -3378415769645309514L;

	// region ----- of
	/**
	 * 创建Db<br>
	 * 使用默认数据源，自动探测数据库连接池
	 *
	 * @return Db
	 */
	public static Db of() {
		return of(DSUtil.getDS());
	}

	/**
	 * 创建Db<br>
	 * 使用默认数据源，自动探测数据库连接池
	 *
	 * @param group 数据源分组
	 * @return Db
	 */
	public static Db of(final String group) {
		return of(DSUtil.getDS(group));
	}

	/**
	 * 创建Db<br>
	 * 会根据数据源连接的元信息识别目标数据库类型，进而使用合适的数据源
	 *
	 * @param ds 数据源
	 * @return Db
	 */
	public static Db of(final DataSource ds) {
		return of(ds, DialectFactory.getDialect(ds));
	}

	/**
	 * 创建Db
	 *
	 * @param config 数据库配置
	 * @return Db
	 */
	public static Db of(final DbConfig config){
		final DSWrapper ds = DSUtil.createDS(config);
		final Dialect dialect = ObjUtil.defaultIfNull(config.getDialect(), DialectFactory.newDialect(ds));
		return of(ds, dialect);
	}

	/**
	 * 创建Db
	 *
	 * @param ds 数据源
	 * @param dialect 方言
	 * @return Db
	 */
	public static Db of(final DataSource ds, final Dialect dialect) {
		return new Db(ds, dialect);
	}
	// endregion

	/**
	 * 构造
	 *
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public Db(final DataSource ds, final Dialect dialect) {
		super(ds, dialect);
	}

	/**
	 * 执行事务，使用默认的事务级别<br>
	 * 在同一事务中，所有对数据库操作都是原子的，同时提交或者同时回滚
	 *
	 * @param func 事务函数，所有操作应在同一函数下执行，确保在同一事务中
	 * @return this
	 * @throws SQLException SQL异常
	 */
	public Db tx(final SerConsumer<Db> func) throws SQLException {
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
	public Db tx(final TransactionLevel transactionLevel, final SerConsumer<Db> func) throws SQLException {
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
		final boolean autoCommit = conn.getAutoCommit();
		if (autoCommit) {
			conn.setAutoCommit(false);
		}

		// 执行事务
		try {
			func.accept(this);
			// 提交
			conn.commit();
		} catch (final Throwable e) {
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
	private void quietRollback(final Connection conn) {
		if (null != conn) {
			try {
				conn.rollback();
			} catch (final Exception e) {
				LogUtil.error(e);
			}
		}
	}

	/**
	 * 静默设置自动提交
	 *
	 * @param conn Connection
	 * @param autoCommit 是否自动提交
	 */
	private void quietSetAutoCommit(final Connection conn, final Boolean autoCommit) {
		if (null != conn && null != autoCommit) {
			try {
				conn.setAutoCommit(autoCommit);
			} catch (final Exception e) {
				LogUtil.error(e);
			}
		}
	}
	// ---------------------------------------------------------------------------- Private method end
}
