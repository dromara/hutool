package cn.hutool.db.transaction;

import java.sql.Connection;

/**
 * 事务级别枚举
 * 
 * @see Connection#TRANSACTION_NONE
 * @see Connection#TRANSACTION_READ_UNCOMMITTED
 * @see Connection#TRANSACTION_READ_COMMITTED
 * @see Connection#TRANSACTION_REPEATABLE_READ
 * @see Connection#TRANSACTION_SERIALIZABLE
 * @author looly
 * @since 4.1.2
 */
public enum TransactionLevel {
	/** 驱动不支持事务 */
	NONE(Connection.TRANSACTION_NONE),
	/** 允许脏读、不可重复读和幻读 */
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
	/** 禁止脏读，但允许不可重复读和幻读 */
	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
	/** 禁止脏读和不可重复读，单运行幻读 */
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
	/** 禁止脏读、不可重复读和幻读 */
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

	/** 事务级别，对应Connection中的常量值 */
	private int level;

	private TransactionLevel(int level) {
		this.level = level;
	}

	/**
	 * 获取数据库事务级别int值
	 * 
	 * @return 数据库事务级别int值
	 */
	public int getLevel() {
		return this.level;
	}
}
