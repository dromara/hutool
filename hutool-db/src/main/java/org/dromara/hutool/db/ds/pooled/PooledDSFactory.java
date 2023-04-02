/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;

import javax.sql.DataSource;

/**
 * Hutool自身实现的池化数据源工厂类
 *
 * @author Looly
 *
 */
public class PooledDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 8093886210895248277L;

	/**
	 * 数据源名称：Hutool-Pooled-DataSource
	 */
	public static final String DS_NAME = "Hutool-Pooled-DataSource";

	/**
	 * 构造，使用默认配置文件
	 */
	public PooledDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public PooledDSFactory(final Setting setting) {
		super(DS_NAME, PooledDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final DbConfig dbConfig = new DbConfig();
		dbConfig.setUrl(jdbcUrl);
		dbConfig.setDriver(driver);
		dbConfig.setUser(user);
		dbConfig.setPass(pass);

		// 连接池相关信息
		dbConfig.setInitialSize(poolSetting.getInt("initialSize", 0));
		dbConfig.setMinIdle(poolSetting.getInt("minIdle", 0));
		dbConfig.setMaxActive(poolSetting.getInt("maxActive", 8));
		dbConfig.setMaxWait(poolSetting.getLong("maxWait", 6000L));

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.get(key);
			if(StrUtil.isNotBlank(connValue)){
				dbConfig.addConnProps(key, connValue);
			}
		}

		return new PooledDataSource(dbConfig);
	}
}
