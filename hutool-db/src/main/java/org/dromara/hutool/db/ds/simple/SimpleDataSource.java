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

package org.dromara.hutool.db.ds.simple;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/***
 * 简易数据源，没有使用连接池，仅供测试或打开关闭连接非常少的场合使用！
 *
 * @author loolly
 *
 */
public class SimpleDataSource extends AbstractDataSource {

	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";

	private final DbConfig config;

	// -------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public SimpleDataSource() {
		this(StrUtil.EMPTY);
	}

	/**
	 * 构造
	 *
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(final String group) {
		this(null, group);
	}

	/**
	 * 构造
	 *
	 * @param setting 数据库配置
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(Setting setting, final String group) {
		if (null == setting) {
			setting = new Setting(DEFAULT_DB_CONFIG_PATH);
		}
		final Setting dbSetting = setting.getSetting(group);
		if (MapUtil.isEmpty(dbSetting)) {
			throw new DbRuntimeException("No DataSource config for group: [{}]", group);
		}

		final DbConfig dbConfig = new DbConfig();

		// 基本信息
		final String url = dbSetting.getAndRemove(DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}
		dbConfig.setUrl(url);

		// 自动识别Driver
		final String driver = dbSetting.getAndRemove(DSKeys.KEY_ALIAS_DRIVER);
		dbConfig.setDriver(StrUtil.isNotBlank(driver) ? driver : DriverUtil.identifyDriver(url));
		dbConfig.setUser(dbSetting.getAndRemove(DSKeys.KEY_ALIAS_USER));
		dbConfig.setPass(dbSetting.getAndRemove(DSKeys.KEY_ALIAS_PASSWORD));

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = dbSetting.get(key);
			if(StrUtil.isNotBlank(connValue)){
				dbConfig.addConnProps(key, connValue);
			}
		}

		this.config = dbConfig;
	}

	/**
	 * 构造
	 *
	 * @param config 数据库连接配置
	 */
	public SimpleDataSource(final DbConfig config) {
		this.config = config;
	}
	// -------------------------------------------------------------------- Constructor end

	@Override
	public Connection getConnection() throws SQLException {
		final DbConfig config = this.config;
		final Props info = new Props();

		final String user = config.getUser();
		if (user != null) {
			info.setProperty("user", user);
		}
		final String pass = config.getPass();
		if (pass != null) {
			info.setProperty("password", pass);
		}

		// 其它参数
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			info.putAll(connProps);
		}

		return DriverManager.getConnection(config.getUrl(), info);
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return DriverManager.getConnection(config.getUrl(), username, password);
	}

	@Override
	public void close() {
		// Not need to close;
	}
}
