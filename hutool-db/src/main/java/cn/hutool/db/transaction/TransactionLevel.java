package cn.hutool.db.transaction;

import java.sql.Connection;

/**
 * 事务级别枚举
 * 
 * <p>
 * <b>脏读（Dirty Read）</b>：<br>
 * 一个事务会读到另一个事务更新后但未提交的数据，如果另一个事务回滚，那么当前事务读到的数据就是脏数据
 * <p>
 * <b>不可重复读（Non Repeatable Read）</b>：<br>
 * 在一个事务内，多次读同一数据，在这个事务还没有结束时，如果另一个事务恰好修改了这个数据，那么，在第一个事务中，两次读取的数据就可能不一致
 * <p>
 * <b>幻读（Phantom Read）</b>：<br>
 * 在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，且可以再次读取同一条记录。
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

	/**
	 * 允许脏读、不可重复读和幻读
	 * <p>
	 * 在这种隔离级别下，一个事务会读到另一个事务更新后但未提交的数据，如果另一个事务回滚，那么当前事务读到的数据就是脏数据，这就是脏读（Dirty Read）
	 */
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

	/**
	 * 禁止脏读，但允许不可重复读和幻读
	 * <p>
	 * 此级别下，一个事务可能会遇到不可重复读（Non Repeatable Read）的问题。<br>
	 * 不可重复读是指，在一个事务内，多次读同一数据，在这个事务还没有结束时，如果另一个事务恰好修改了这个数据，那么，在第一个事务中，两次读取的数据就可能不一致。
	 */
	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

	/**
	 * 禁止脏读和不可重复读，但允许幻读，MySQL的InnoDB引擎默认使用此隔离级别。
	 * <p>
	 * 此级别下，一个事务可能会遇到幻读（Phantom Read）的问题。<br>
	 * 幻读是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，可以成功，且可以再次读取同一条记录。
	 */
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

	/**
	 * 禁止脏读、不可重复读和幻读
	 * <p>
	 * 虽然Serializable隔离级别下的事务具有最高的安全性，但是，由于事务是串行执行，所以效率会大大下降，应用程序的性能会急剧降低。
	 */
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

	/** 事务级别，对应Connection中的常量值 */
	private final int level;

	TransactionLevel(int level) {
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
