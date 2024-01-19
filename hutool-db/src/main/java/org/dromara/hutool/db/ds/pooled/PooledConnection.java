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

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.pool.Poolable;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.setting.props.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 池化
 *
 * @author Looly
 */
public class PooledConnection extends ConnectionWrapper implements Poolable<Connection> {

	private final PooledDataSource dataSource;

	private long lastBorrow = System.currentTimeMillis();
	private boolean isClosed = false;

	/**
	 * 构造
	 *
	 * @param config 数据库配置
	 * @param dataSource 数据源
	 */
	public PooledConnection(final ConnectionConfig<?> config, final PooledDataSource dataSource) {
		final Props info = new Props();
		final String user = config.getUser();
		if (user != null) {
			info.setProperty("user", user);
		}
		final String password = config.getPass();
		if (password != null) {
			info.setProperty("password", password);
		}

		// 其它参数
		final Properties connProps = config.getConnProps();
		if (MapUtil.isNotEmpty(connProps)) {
			info.putAll(connProps);
		}

		try {
			this.raw = DriverManager.getConnection(config.getUrl(), info);
		} catch (final SQLException e) {
			throw new DbException(e);
		}

		this.dataSource = dataSource;
	}

	@Override
	public void close() {
		this.isClosed = true;
		dataSource.returnObject(this);
	}

	@Override
	public boolean isClosed() {
		return this.isClosed;
	}

	@Override
	public long getLastBorrow() {
		return lastBorrow;
	}

	@Override
	public void setLastBorrow(final long lastBorrow) {
		this.lastBorrow = lastBorrow;
	}
}
