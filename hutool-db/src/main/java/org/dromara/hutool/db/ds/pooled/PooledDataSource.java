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

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.pool.ObjectFactory;
import org.dromara.hutool.core.pool.ObjectPool;
import org.dromara.hutool.core.pool.partition.PartitionObjectPool;
import org.dromara.hutool.core.pool.partition.PartitionPoolConfig;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.db.ds.simple.AbstractDataSource;
import org.dromara.hutool.log.LogUtil;
import org.dromara.hutool.setting.props.Props;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

/**
 * 池化的数据源，用于管理数据库连接
 *
 * @author Looly
 * @since 6.0.0
 */
public class PooledDataSource extends AbstractDataSource {

	private static final String KEY_MAX_WAIT = "maxWait";
	private static final String KEY_INITIAL_SIZE = "initialSize";
	private static final String KEY_MAX_ACTIVE = "maxActive";

	protected Driver driver;
	private final int maxWait;
	private final ObjectPool<PooledConnection> connPool;

	/**
	 * 构造
	 *
	 * @param config 数据库池配置
	 */
	public PooledDataSource(final ConnectionConfig<?> config) {

		final String driverName = config.getDriver();
		if (StrUtil.isNotBlank(driverName)) {
			this.driver = DriverUtil.createDriver(driverName);
		}
		final Props poolProps = Props.of(config.getPoolProps());
		this.maxWait = poolProps.getInt(KEY_MAX_WAIT, 6000);

		final PartitionPoolConfig poolConfig = (PartitionPoolConfig) PartitionPoolConfig.of()
			.setPartitionSize(1)
			.setMaxWait(this.maxWait)
			.setMinSize(poolProps.getInt(KEY_INITIAL_SIZE, 0))
			.setMaxSize(poolProps.getInt(KEY_MAX_ACTIVE, 8));

		this.connPool = new PartitionObjectPool<>(poolConfig, createConnFactory(config));
	}

	/**
	 * 设置驱动
	 *
	 * @param driver 驱动
	 * @return this
	 */
	public PooledDataSource setDriver(final Driver driver) {
		this.driver = driver;
		return this;
	}

	@Override
	public PooledConnection getConnection() throws SQLException {
		return connPool.borrowObject();
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
	private ObjectFactory<PooledConnection> createConnFactory(final ConnectionConfig<?> config) {
		return new ObjectFactory<PooledConnection>() {
			@Override
			public PooledConnection create() {
				return new PooledConnection(config, PooledDataSource.this);
			}

			@Override
			public boolean validate(final PooledConnection connection) {
				try {
					return null != connection
						&& connection.isValid(maxWait);
				} catch (final SQLException e) {
					// 验证对象时抛出异常会导致资源无法释放或回收，此处忽略之
					LogUtil.error(e);
					//throw new DbException(e);
					return false;
				}
			}

			@Override
			public void destroy(final PooledConnection connection) {
				if(null != connection){
					connection.destroy();
				}
			}
		};
	}
}
