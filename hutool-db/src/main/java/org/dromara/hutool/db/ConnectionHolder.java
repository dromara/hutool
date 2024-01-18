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

import java.sql.Connection;

/**
 * 控制{@link Connection}获取和关闭
 *
 * @author looly
 * @since 6.0.0
 */
public interface ConnectionHolder {
	/**
	 * 获得链接。根据实现不同，可以自定义获取连接的方式
	 *
	 * @return {@link Connection}
	 * @throws DbException 连接获取异常
	 */
	Connection getConnection() throws DbException;

	/**
	 * 关闭连接<br>
	 * 自定义关闭连接有利于自定义回收连接机制，或者不关闭
	 *
	 * @param conn 连接 {@link Connection}
	 */
	void closeConnection(Connection conn);
}
