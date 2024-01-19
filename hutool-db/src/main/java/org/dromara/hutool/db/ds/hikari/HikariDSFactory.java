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

package org.dromara.hutool.db.ds.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * HikariCP数据源工厂类
 *
 * @author Looly
 *
 */
public class HikariDSFactory implements DSFactory {
	private static final long serialVersionUID = -8834744983614749401L;

	@Override
	public String getDataSourceName() {
		return "HikariCP";
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
