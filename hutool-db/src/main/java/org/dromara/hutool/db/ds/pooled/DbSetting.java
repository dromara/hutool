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

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.dialect.DriverUtil;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;

/**
 * 数据库配置文件类，此类对应一个数据库配置文件
 *
 * @author Looly
 *
 */
public class DbSetting {
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";

	private final Setting setting;

	/**
	 * 构造
	 */
	public DbSetting() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param setting 数据库配置
	 */
	public DbSetting(final Setting setting) {
		if (null == setting) {
			this.setting = new Setting(DEFAULT_DB_CONFIG_PATH);
		} else {
			this.setting = setting;
		}
	}

	/**
	 * 获得数据库连接信息
	 *
	 * @param group 分组
	 * @return 分组
	 */
	public DbConfig getDbConfig(final String group) {
		final Setting config = setting.getSetting(group);
		if (MapUtil.isEmpty(config)) {
			throw new DbRuntimeException("No Hutool pool config for group: [{}]", group);
		}

		final DbConfig dbConfig = new DbConfig();

		// 基本信息
		final String url = config.getAndRemove(DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}
		dbConfig.setUrl(url);
		// 自动识别Driver
		final String driver = config.getAndRemove(DSKeys.KEY_ALIAS_DRIVER);
		dbConfig.setDriver(StrUtil.isNotBlank(driver) ? driver : DriverUtil.identifyDriver(url));
		dbConfig.setUser(config.getAndRemove(DSKeys.KEY_ALIAS_USER));
		dbConfig.setPass(config.getAndRemove(DSKeys.KEY_ALIAS_PASSWORD));

		// 连接池相关信息
		dbConfig.setInitialSize(setting.getIntByGroup("initialSize", group, 0));
		dbConfig.setMinIdle(setting.getIntByGroup("minIdle", group, 0));
		dbConfig.setMaxActive(setting.getIntByGroup("maxActive", group, 8));
		dbConfig.setMaxWait(setting.getLongByGroup("maxWait", group, 6000L));

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = config.get(key);
			if(StrUtil.isNotBlank(connValue)){
				dbConfig.addConnProps(key, connValue);
			}
		}

		return dbConfig;
	}
}
