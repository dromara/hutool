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

package org.dromara.hutool.db.ds.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * C3P0数据源工厂类
 *
 * @author Looly
 *
 */
public class C3p0DSFactory implements DSFactory {
	private static final long serialVersionUID = -6090788225842047281L;

	@Override
	public String getDataSourceName() {
		return "C3P0";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final ComboPooledDataSource ds = new ComboPooledDataSource();

		ds.setJdbcUrl(config.getUrl());
		try {
			ds.setDriverClass(config.getDriver());
		} catch (final PropertyVetoException e) {
			throw new DbException(e);
		}
		ds.setUser(config.getUser());
		ds.setPassword(config.getPass());

		// 连接池和其它选项
		Props.of(config.getPoolProps()).toBean(ds);

		// 连接配置
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			ds.setProperties(connProps);
		}

		return ds;
	}
}
