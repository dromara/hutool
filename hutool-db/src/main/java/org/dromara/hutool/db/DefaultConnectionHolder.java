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
