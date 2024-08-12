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

package org.dromara.hutool.db.ds.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.AbstractDSFactory;
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
public class C3p0DSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public C3p0DSFactory() {
		super(ComboPooledDataSource.class, "C3P0");
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
			ds.getProperties().putAll(connProps);
		}

		return ds;
	}
}
