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

package org.dromara.hutool.db.ds.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * HikariCP数据源工厂类
 *
 * @author Looly
 *
 */
public class HikariDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public HikariDSFactory() {
		super(HikariDataSource.class, "HikariCP");
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final Props props = new Props();

		// 基本信息
		props.put("jdbcUrl", config.getUrl());
		final String driver = config.getDriver();
		if (null != driver) {
			props.put("driverClassName", driver);
		}
		final String user = config.getUser();
		if (null != user) {
			props.put("username", user);
		}
		final String pass = config.getPass();
		if (null != pass) {
			props.put("password", pass);
		}

		// 连接池信息
		final Properties poolProps = config.getPoolProps();
		if(MapUtil.isNotEmpty(poolProps)){
			props.putAll(poolProps);
		}

		final HikariConfig hikariConfig = new HikariConfig(props);
		// 连接信息
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			hikariConfig.setDataSourceProperties(connProps);
		}

		return new HikariDataSource(hikariConfig);
	}

}
