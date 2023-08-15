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

package org.dromara.hutool.db.ds.druid;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * Druid数据源工厂类
 *
 * @author Looly
 *
 */
public class DruidDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4680621702534433222L;

	/**
	 * 数据源名称：Druid
	 */
	public static final String DS_NAME = "Druid";

	/**
	 * 构造，使用默认配置文件
	 */
	public DruidDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public DruidDSFactory(final Setting setting) {
		super(DS_NAME, DruidDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final DruidDataSource ds = new DruidDataSource();

		// 基本信息
		ds.setUrl(jdbcUrl);
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		// Druid中也可以通过 druid.connectProperties 属性设置
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				ds.addConnectionProperty(key, connValue);
			}
		}

		// Druid连接池配置信息，规范化属性名
		final Props druidProps = new Props();
		poolSetting.forEach((key, value)-> druidProps.put(StrUtil.addPrefixIfNot(key, "druid."), value));
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
