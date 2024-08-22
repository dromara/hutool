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

package org.dromara.hutool.db.ds.tomcat;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.setting.props.Props;

import java.util.Properties;

/**
 * Tomcat-Jdbc-Pool数据源工厂类
 *
 * @author Looly
 */
public class TomcatDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public TomcatDSFactory() {
		super(DataSource.class, "Tomcat-Jdbc-Pool");
	}

	@Override
	public javax.sql.DataSource createDataSource(final ConnectionConfig<?> config) {
		final PoolProperties poolProps = new PoolProperties();

		// 基本配置
		poolProps.setUrl(config.getUrl());
		poolProps.setDriverClassName(config.getDriver());
		poolProps.setUsername(config.getUser());
		poolProps.setPassword(config.getPass());

		// 连接配置
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			poolProps.setDbProperties(connProps);
		}

		// 连接池相关参数
		Props.of(config.getPoolProps()).toBean(poolProps);

		return new DataSource(poolProps);
	}
}
