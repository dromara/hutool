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

package org.dromara.hutool.socket.nio;

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
	Operation(final int value) {
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
