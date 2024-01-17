/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.config;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.resource.NoResourceException;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;

import java.util.Set;

/**
 * 基于{@link Setting}类型配置的数据库配置解析
 *
 * @author Looly
 */
public class SettingConfigParser implements ConfigParser {

	private static final String CONNECTION_PREFIX = "connection.";
	/**
	 * 数据库配置文件可选路径1
	 */
	private static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	/**
	 * 数据库配置文件可选路径2
	 */
	private static final String DEFAULT_DB_SETTING_PATH2 = "db.setting";

	/**
	 * 创建默认配置解析器
	 *
	 * @return SettingConfigParser
	 */
	public static SettingConfigParser of() {
		return new SettingConfigParser(null);
	}

	private final Setting setting;

	/**
	 * 构造
	 *
	 * @param setting 自定义配置Setting，{@code null}表示使用默认配置文件，文件位于"config/db.setting"或"db.setting"
	 */
	public SettingConfigParser(final Setting setting) {
		this.setting = null != setting ? setting : createDefaultSetting();
	}

	@Override
	public DbConfig parse(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final Setting subSetting = setting.getSetting(group);
		if (MapUtil.isEmpty(subSetting)) {
			throw new DbRuntimeException("No config for group: [{}]", group);
		}

		return toDbConfig(subSetting);
	}

	/**
	 * 获取自定义或默认位置数据库配置{@link Setting}
	 *
	 * @return 数据库配置
	 * @since 5.8.0
	 */
	private static Setting createDefaultSetting() {
		Setting setting;
		try {
			setting = new Setting(DEFAULT_DB_SETTING_PATH, true);
		} catch (final NoResourceException e) {
			// 尝试ClassPath下直接读取配置文件
			try {
				setting = new Setting(DEFAULT_DB_SETTING_PATH2, true);
			} catch (final NoResourceException e2) {
				throw new NoResourceException("Default db setting [{}] or [{}] in classpath not found !", DEFAULT_DB_SETTING_PATH, DEFAULT_DB_SETTING_PATH2);
			}
		}
		return setting;
	}

	/**
	 * {@link Setting}数据库配置 转 {@link DbConfig}
	 *
	 * @param setting {@link Setting}数据库配置
	 * @return {@link DbConfig}
	 */
	private static DbConfig toDbConfig(final Setting setting) {
		// 基本信息
		final String url = setting.getAndRemove(DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL!");
		}

		// 移除用户可能误加入的show sql配置项
		// issue#I3VW0R@Gitee
		removeShowSqlParams(setting);

		// 自动识别Driver
		String driver = setting.getAndRemove(DSKeys.KEY_ALIAS_DRIVER);
		if (StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}

		final DbConfig dbConfig = DbConfig.of()
			.setUrl(url)
			.setDriver(driver)
			.setUser(setting.getAndRemove(DSKeys.KEY_ALIAS_USER))
			.setPass(setting.getAndRemove(DSKeys.KEY_ALIAS_PASSWORD));

		// 大小写等配置
		final String caseInsensitive = setting.getAndRemove(DSKeys.KEY_CASE_INSENSITIVE);
		if (StrUtil.isNotBlank(caseInsensitive)) {
			dbConfig.setCaseInsensitive(Convert.toBoolean(caseInsensitive));
		}
		final String returnGeneratedKey = setting.getAndRemove(DSKeys.KEY_RETURN_GENERATED_KEY);
		if (StrUtil.isNotBlank(returnGeneratedKey)) {
			dbConfig.setReturnGeneratedKey(Convert.toBoolean(returnGeneratedKey));
		}

		// remarks等连接配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = setting.getAndRemove(key);
			if (StrUtil.isNotBlank(connValue)) {
				dbConfig.addConnProps(key, connValue);
			}
		}

		// 自定义连接属性
		final Props connProps = new Props();
		final Set<String> keys = setting.keySet();
		for (final String key : keys) {
			if (key.startsWith(CONNECTION_PREFIX)) {
				connProps.set(StrUtil.subSuf(key, CONNECTION_PREFIX.length()), setting.remove(key));
			}
		}
		dbConfig.setConnProps(connProps);

		// 池属性
		dbConfig.setPoolProps(setting.toProps());

		return dbConfig;
	}

	/**
	 * 移除配置文件中的Show SQL相关配置项<br>
	 * 此方法用于移除用户配置在分组下的配置项目
	 *
	 * @param setting 配置项
	 * @since 5.7.2
	 */
	private static void removeShowSqlParams(final Setting setting) {
		setting.remove(DSKeys.KEY_SHOW_SQL);
		setting.remove(DSKeys.KEY_FORMAT_SQL);
		setting.remove(DSKeys.KEY_SHOW_PARAMS);
		setting.remove(DSKeys.KEY_SQL_LEVEL);
	}
}
