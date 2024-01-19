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

package org.dromara.hutool.db.ds.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * DBCP2数据源工厂类
 *
 * @author Looly
 *
 */
public class DbcpDSFactory implements DSFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public String getDataSourceName() {
		return "commons-dbcp2";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final BasicDataSource ds = new BasicDataSource();

		ds.setUrl(config.getUrl());
		ds.setDriverClassName(config.getDriver());
		ds.setUsername(config.getUser());
		ds.setPassword(config.getPass());

		// 连接池和其它选项
		Props.of(config.getPoolProps()).toBean(ds);

		// 连接配置
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			connProps.forEach((key, value)->ds.addConnectionProperty(key.toString(), value.toString()));
		}

		return ds;
	}
}
