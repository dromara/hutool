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
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.dialect.DialectFactory;
import org.dromara.hutool.db.ds.DSUtil;
import org.dromara.hutool.log.Log;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * 数据库SQL执行会话<br>
 * 会话通过共用Connection而可以实现JDBC事务<br>
 * 一个会话只维护一个连接，推荐在执行完后关闭Session，避免重用<br>
 * 本对象并不是线程安全的，多个线程共用一个Session将会导致不可预知的问题
 *
 * @author loolly
 *
 */
public class Session extends AbstractDb<Session> implements Closeable {
	private static final long serialVersionUID = 3421251905539056945L;
	private final static Log log = Log.get();

	/**
	 * 创建默认数据源会话
	 *
	 * @return Session
	 * @since 3.2.3
	 */
	public static Session of() {
		return new Session(DSUtil.getDS());
	}

	/**
	 * 创建会话
	 *
	 * @param group 分组
	 * @return Session
	 * @since 4.0.11
	 */
	public static Session of(final String group) {
		return new Session(DSUtil.getDS(group));
	}

	/**
	 * 创建会话
	 *
	 * @param ds 数据源
	 * @return Session
	 */
	public static Session of(final DataSource ds) {
		return new Session(ds);
	}

	// ---------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，从DataSource中识别方言
	 *
	 * @param ds 数据源
	 */
	public Session(final DataSource ds) {
		this(ds, DialectFactory.getDialect(ds));
	}

	/**
	 * 构造
	 *
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public Session(final DataSource ds, final Dialect dialect) {
		super(ds, dialect);
	}
	// ---------------------------------------------------------------------------- Constructor end

	// ---------------------------------------------------------------------------- Transaction method start
	/**
	 * 开始事务
	 *
	 * @throws DbException SQL执行异常
	 */
	public void beginTransaction() throws DbException {
		final Connection conn = getConnection();
		checkTransactionSupported(conn);
		try {
			conn.setAutoCommit(false);
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 提交事务
	 *
	 * @throws DbException SQL执行异常
	 */
	public void commit() throws DbException {
		try {
			getConnection().commit();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (final SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 回滚事务
	 *
	 * @throws DbException SQL执行异常
	 */
	public void rollback() throws DbException {
		try {
			getConnection().rollback();
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (final SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 静默回滚事务<br>
	 * 回滚事务
	 */
	public void quietRollback() {
		try {
			getConnection().rollback();
		} catch (final Exception e) {
			log.error(e);
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (final SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 *
	 * @param savepoint 保存点
	 * @throws DbException SQL执行异常
	 */
	public void rollback(final Savepoint savepoint) throws DbException {
		try {
			getConnection().rollback(savepoint);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (final SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 静默回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 *
	 * @param savepoint 保存点
	 */
	public void quietRollback(final Savepoint savepoint) {
		try {
			getConnection().rollback(savepoint);
		} catch (final Exception e) {
			log.error(e);
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (final SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 设置保存点
	 *
	 * @return 保存点对象
	 * @throws DbException SQL执行异常
	 */
	public Savepoint setSavepoint() throws DbException {
		try {
			return getConnection().setSavepoint();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 设置保存点
	 *
	 * @param name 保存点的名称
	 * @return 保存点对象
	 * @throws SQLException SQL执行异常
	 */
	public Savepoint setSavepoint(final String name) throws SQLException {
		return getConnection().setSavepoint(name);
	}

	/**
	 * 设置事务的隔离级别<br>
	 *
	 * Connection.TRANSACTION_NONE 驱动不支持事务<br>
	 * Connection.TRANSACTION_READ_UNCOMMITTED 允许脏读、不可重复读和幻读<br>
	 * Connection.TRANSACTION_READ_COMMITTED 禁止脏读，但允许不可重复读和幻读<br>
	 * Connection.TRANSACTION_REPEATABLE_READ 禁止脏读和不可重复读，单运行幻读<br>
	 * Connection.TRANSACTION_SERIALIZABLE 禁止脏读、不可重复读和幻读<br>
	 *
	 * @param level 隔离级别
	 * @throws DbException SQL执行异常
	 */
	public void setTransactionIsolation(final int level) throws DbException {
		try {
			if (getConnection().getMetaData().supportsTransactionIsolationLevel(level) == false) {
				throw new DbException(StrUtil.format("Transaction isolation [{}] not support!", level));
			}
			getConnection().setTransactionIsolation(level);
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 在事务中执行操作，通过实现{@link SerConsumer}接口的call方法执行多条SQL语句从而完成事务
	 *
	 * @param func 函数抽象，在函数中执行多个SQL操作，多个操作会被合并为同一事务
	 * @throws DbException SQL异常
	 * @since 3.2.3
	 */
	public void tx(final SerConsumer<Session> func) throws DbException {
		try {
			beginTransaction();
			func.accept(this);
			commit();
		} catch (final Throwable e) {
			quietRollback();
			throw new DbException(e);
		}
	}

	// ---------------------------------------------------------------------------- Transaction method end

	@Override
	public void close() {
		closeConnection(null);
	}
}
