/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.pool.ObjectFactory;
import org.dromara.hutool.core.pool.ObjectPool;
import org.dromara.hutool.core.pool.partition.PartitionObjectPool;
import org.dromara.hutool.core.pool.partition.PartitionPoolConfig;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.ds.simple.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 池化的数据源，用于管理数据库连接
 *
 * @author Looly
 * @since 6.0.0
 */
public class PooledDataSource extends AbstractDataSource {

	private final ObjectPool<Connection> connPool;

	/**
	 * 构造
	 *
	 * @param config 数据库池配置
	 */
	public PooledDataSource(final PooledDbConfig config) {
		final PartitionPoolConfig poolConfig = (PartitionPoolConfig) PartitionPoolConfig.of()
			.setPartitionSize(1)
			.setMaxWait(config.getMaxWait())
			.setMinSize(config.getInitialSize())
			.setMaxSize(config.getMaxActive());

		this.connPool = new PartitionObjectPool<>(poolConfig, createConnFactory(config));
	}

	@Override
	public Connection getConnection() throws SQLException {
		return (Connection) connPool.borrowObject();
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		throw new SQLException("Pooled DataSource is not allow to get special Connection!");
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.connPool);
	}

	/**
	 * 将连接返回到池中
	 *
	 * @param conn {@link PooledConnection}
	 */
	public void returnObject(final PooledConnection conn) {
		this.connPool.returnObject(conn);
	}

	/**
	 * 创建自定义的{@link PooledConnection}工厂类
	 *
	 * @param config 数据库配置
	 * @return {@link ObjectFactory}
	 */
	private ObjectFactory<Connection> createConnFactory(final PooledDbConfig config) {
		return new ObjectFactory<Connection>() {
			@Override
			public Connection create() {
				return new PooledConnection(config, PooledDataSource.this);
			}

			@Override
			public boolean validate(final Connection connection) {
				try {
					return null != connection && connection.isValid((int) config.getMaxWait());
				} catch (final SQLException e) {
					throw new DbRuntimeException(e);
				}
			}

			@Override
			public void destroy(final Connection connection) {
				IoUtil.closeQuietly(connection);
			}
		};
	}
}
