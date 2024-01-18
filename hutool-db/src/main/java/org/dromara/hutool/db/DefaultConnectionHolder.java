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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 默认的连接持有器
 *
 * @author looly
 */
public class DefaultConnectionHolder implements ConnectionHolder {

	protected final DataSource ds;

	/**
	 * 构造
	 *
	 * @param ds {@link DataSource}
	 */
	public DefaultConnectionHolder(final DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Connection getConnection() throws DbException {
		try {
			return ThreadLocalConnection.INSTANCE.get(this.ds);
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	@Override
	public void closeConnection(final Connection conn) {
		try {
			if (conn != null && !conn.getAutoCommit()) {
				// 事务中的Session忽略关闭事件
				return;
			}
		} catch (final SQLException e) {
			// ignore
		}

		ThreadLocalConnection.INSTANCE.close(this.ds);
	}
}
