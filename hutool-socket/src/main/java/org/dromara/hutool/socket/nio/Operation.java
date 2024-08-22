/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
