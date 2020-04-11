package cn.hutool.socket.nio;

import java.nio.channels.SelectionKey;

/**
 * SelectionKey Operation的枚举封装
 * 
 * @author looly
 */
public enum Operation {

	/** 读操作 */
	READ(SelectionKey.OP_READ),
	/** 写操作 */
	WRITE(SelectionKey.OP_WRITE),
	/** 连接操作 */
	CONNECT(SelectionKey.OP_CONNECT),
	/** 接受连接操作 */
	ACCEPT(SelectionKey.OP_ACCEPT);

	private final int value;

	/**
	 * 构造
	 * 
	 * @param value 值
	 * @see SelectionKey#OP_READ
	 * @see SelectionKey#OP_WRITE
	 * @see SelectionKey#OP_CONNECT
	 * @see SelectionKey#OP_ACCEPT
	 */
	Operation(int value) {
		this.value = value;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 * @see SelectionKey#OP_READ
	 * @see SelectionKey#OP_WRITE
	 * @see SelectionKey#OP_CONNECT
	 * @see SelectionKey#OP_ACCEPT
	 */
	public int getValue() {
		return this.value;
	}
}
