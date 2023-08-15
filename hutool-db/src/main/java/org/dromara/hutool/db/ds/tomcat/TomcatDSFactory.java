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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Tomcat-Jdbc-Pool数据源工厂类
 *
 * @author Looly
 *
 */
public class TomcatDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4925514193275150156L;

	/**
	 * 数据源名称：Tomcat-Jdbc-Pool
	 */
	public static final String DS_NAME = "Tomcat-Jdbc-Pool";

	/**
	 * 构造
	 */
	public TomcatDSFactory() {
		this(null);
	}

	/**
	 * 构造，自定义配置
	 *
	 * @param setting Setting数据库配置
	 */
	public TomcatDSFactory(final Setting setting) {
		super(DS_NAME, DataSource.class, setting);
	}

	@Override
	protected javax.sql.DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final PoolProperties poolProps = new PoolProperties();
		poolProps.setUrl(jdbcUrl);
		poolProps.setDriverClassName(driver);
		poolProps.setUsername(user);
		poolProps.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}
		poolProps.setDbProperties(connProps);

		// 连接池相关参数
		poolSetting.toBean(poolProps);

		return new DataSource(poolProps);
	}
}
