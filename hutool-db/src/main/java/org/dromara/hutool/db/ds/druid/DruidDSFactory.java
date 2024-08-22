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

package org.dromara.hutool.db.ds.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Druid数据源工厂类
 *
 * @author Looly
 *
 */
public class DruidDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public DruidDSFactory() {
		super(DruidDataSource.class, "Druid");
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final DruidDataSource ds = new DruidDataSource();

		// 基本信息
		ds.setUrl(config.getUrl());
		ds.setDriverClassName(config.getDriver());
		ds.setUsername(config.getUser());
		ds.setPassword(config.getPass());

		// 连接配置
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			connProps.forEach((key, value)->ds.addConnectionProperty(key.toString(), value.toString()));
		}

		// Druid连接池配置信息，规范化属性名
		final Properties poolProps = config.getPoolProps();
		if(MapUtil.isNotEmpty(poolProps)){
			final Props druidProps = new Props();
			poolProps.forEach((key, value)-> druidProps.set(StrUtil.addPrefixIfNot(key.toString(), "druid."), value));
			ds.configFromPropeties(druidProps);

			//issue#I4ZKCW 某些非属性设置单独设置
			// connectionErrorRetryAttempts
			final String connectionErrorRetryAttemptsKey = "druid.connectionErrorRetryAttempts";
			if(druidProps.containsKey(connectionErrorRetryAttemptsKey)){
				ds.setConnectionErrorRetryAttempts(druidProps.getInt(connectionErrorRetryAttemptsKey));
			}
			// timeBetweenConnectErrorMillis
			final String timeBetweenConnectErrorMillisKey = "druid.timeBetweenConnectErrorMillis";
			if(druidProps.containsKey(timeBetweenConnectErrorMillisKey)){
				ds.setTimeBetweenConnectErrorMillis(druidProps.getInt(timeBetweenConnectErrorMillisKey));
			}
			// breakAfterAcquireFailure
			final String breakAfterAcquireFailureKey = "druid.breakAfterAcquireFailure";
			if(druidProps.containsKey(breakAfterAcquireFailureKey)){
				ds.setBreakAfterAcquireFailure(druidProps.getBool(breakAfterAcquireFailureKey));
			}

			// issue#I8STFC 补充
			// validationQueryTimeout
			final String validationQueryTimeout = "druid.validationQueryTimeout";
			if(druidProps.containsKey(validationQueryTimeout)){
				ds.setValidationQueryTimeout(druidProps.getInt(validationQueryTimeout));
			}
			// queryTimeout
			final String queryTimeout = "druid.queryTimeout";
			if(druidProps.containsKey(queryTimeout)){
				ds.setQueryTimeout(druidProps.getInt(queryTimeout));
			}
			// connectTimeout
			final String connectTimeout = "druid.connectTimeout";
			if(druidProps.containsKey(connectTimeout)){
				ds.setConnectTimeout(druidProps.getInt(connectTimeout));
			}
			// socketTimeout
			final String socketTimeout = "druid.socketTimeout";
			if(druidProps.containsKey(socketTimeout)){
				ds.setSocketTimeout(druidProps.getInt(socketTimeout));
			}
			// transactionQueryTimeout
			final String transactionQueryTimeout = "druid.transactionQueryTimeout";
			if(druidProps.containsKey(transactionQueryTimeout)){
				ds.setTransactionQueryTimeout(druidProps.getInt(transactionQueryTimeout));
			}
			// loginTimeout
			final String loginTimeout = "druid.loginTimeout";
			if(druidProps.containsKey(loginTimeout)){
				ds.setLoginTimeout(druidProps.getInt(loginTimeout));
			}
		}

		// 检查关联配置，在用户未设置某项配置时，设置默认值
		if (null == ds.getValidationQuery()) {
			// 在validationQuery未设置的情况下，以下三项设置都将无效
			ds.setTestOnBorrow(false);
			ds.setTestOnReturn(false);
			ds.setTestWhileIdle(false);
		}

		return ds;
	}
}
