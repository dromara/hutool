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

package org.dromara.hutool.db.ds.simple;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.setting.props.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/***
 * 简易数据源，没有使用连接池，仅供测试或打开关闭连接非常少的场合使用！
 *
 * @author loolly
 *
 */
public class SimpleDataSource extends AbstractDataSource {

	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";

	private final ConnectionConfig<?> config;

	/**
	 * 构造
	 *
	 * @param config 数据库连接配置
	 */
	public SimpleDataSource(final ConnectionConfig<?> config) {
		this.config = config;
	}

	@Override
	public Connection getConnection() throws SQLException {
		final ConnectionConfig<?> config = this.config;
		final Props info = new Props();

		final String user = config.getUser();
		if (user != null) {
			info.setProperty("user", user);
		}
		final String pass = config.getPass();
		if (pass != null) {
			info.setProperty("password", pass);
		}

		// 其它参数
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			info.putAll(connProps);
		}

		return DriverManager.getConnection(config.getUrl(), info);
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return DriverManager.getConnection(config.getUrl(), username, password);
	}

	@Override
	public void close() {
		// Not need to close;
	}
}
