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

package org.dromara.hutool.db.ds.tomcat;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.db.ds.DbConfig;
import org.dromara.hutool.setting.props.Props;

/**
 * Tomcat-Jdbc-Pool数据源工厂类
 *
 * @author Looly
 */
public class TomcatDSFactory implements DSFactory {
	private static final long serialVersionUID = 4925514193275150156L;

	@Override
	public String getDataSourceName() {
		return "Tomcat-Jdbc-Pool";
	}

	@Override
	public javax.sql.DataSource createDataSource(final DbConfig config) {
		final PoolProperties poolProps = new PoolProperties();

		// 基本配置
		poolProps.setUrl(config.getUrl());
		poolProps.setDriverClassName(config.getDriver());
		poolProps.setUsername(config.getUser());
		poolProps.setPassword(config.getPass());

		// 连接配置
		poolProps.setDbProperties(config.getConnProps());

		// 连接池相关参数
		Props.of(config.getPoolProps()).toBean(poolProps);

		return new DataSource(poolProps);
	}
}
