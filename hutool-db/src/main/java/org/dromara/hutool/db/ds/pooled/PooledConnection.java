/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.pool.Poolable;
import org.dromara.hutool.core.text.StrUtil;
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
		// issue#IA6EUQ 部分驱动无法自动加载，此处手动完成
		final String driver = config.getDriver();
		if(StrUtil.isNotBlank(driver)){
			try {
				Class.forName(driver);
			} catch (final ClassNotFoundException e) {
				throw new DbException(e);
			}
		}

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
			if(null != dataSource.driver){
				this.raw = dataSource.driver.connect(config.getUrl(), info);
			}else{
				this.raw = DriverManager.getConnection(config.getUrl(), info);
			}
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
	public long getLastReturn() {
		return lastBorrow;
	}

	@Override
	public void setLastReturn(final long lastReturn) {
		this.lastBorrow = lastReturn;
	}
}
