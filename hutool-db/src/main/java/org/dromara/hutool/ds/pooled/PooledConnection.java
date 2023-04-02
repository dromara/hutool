/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.ds.pooled;

import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.dialect.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 池化
 *
 * @author Looly
 */
public class PooledConnection extends ConnectionWraper {

	private final PooledDataSource ds;
	private boolean isClosed;

	/**
	 * 构造
	 *
	 * @param ds 数据源
	 * @throws SQLException SQL异常
	 */
	public PooledConnection(final PooledDataSource ds) throws SQLException {
		this.ds = ds;
		final DbConfig config = ds.getConfig();

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

		this.raw = DriverManager.getConnection(config.getUrl(), info);
	}

	/**
	 * 构造
	 *
	 * @param ds   {@link PooledDataSource}
	 * @param conn {@link Connection}
	 */
	public PooledConnection(final PooledDataSource ds, final Connection conn) {
		this.ds = ds;
		this.raw = conn;
	}

	/**
	 * 重写关闭连接，实际操作是归还到连接池中
	 */
	@Override
	public void close() {
		this.ds.free(this);
		this.isClosed = true;
	}

	/**
	 * 连接是否关闭，关闭条件：<br>
	 * 1、被归还到池中
	 * 2、实际连接已关闭
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return isClosed || raw.isClosed();
	}

	/**
	 * 打开连接
	 *
	 * @return this
	 */
	protected PooledConnection open() {
		this.isClosed = false;
		return this;
	}

	/**
	 * 释放连接
	 *
	 * @return this
	 */
	protected PooledConnection release() {
		IoUtil.closeQuietly(this.raw);
		return this;
	}
}
