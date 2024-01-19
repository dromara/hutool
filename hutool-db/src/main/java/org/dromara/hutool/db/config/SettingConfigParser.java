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
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.db.sql.SqlLog;
import org.dromara.hutool.db.sql.filter.SqlLogFilter;
import org.dromara.hutool.log.level.Level;
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
		return of(null);
	}

	/**
	 * 创建默认配置解析器
	 *
	 * @param setting 配置文件
	 * @return SettingConfigParser
	 */
	public static SettingConfigParser of(final Setting setting) {
		return new SettingConfigParser(setting);
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
	public DbConfig parse(final String group) {
		final Setting setting = this.setting;
		final Setting subSetting = setting.getSetting(StrUtil.emptyIfNull(group));
		if (MapUtil.isEmpty(subSetting)) {
			throw new DbException("No config for group: [{}]", group);
		}

		// 继承属性
		subSetting.putIfAbsent(DSKeys.KEY_SHOW_SQL, setting.get(DSKeys.KEY_SHOW_SQL));
		subSetting.putIfAbsent(DSKeys.KEY_FORMAT_SQL, setting.get(DSKeys.KEY_FORMAT_SQL));
		subSetting.putIfAbsent(DSKeys.KEY_SHOW_PARAMS, setting.get(DSKeys.KEY_SHOW_PARAMS));
		subSetting.putIfAbsent(DSKeys.KEY_SQL_LEVEL, setting.get(DSKeys.KEY_SQL_LEVEL));

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
			throw new DbException("No JDBC URL!");
		}

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

		// SQL日志
		final SqlLogFilter sqlLogFilter = getSqlLogFilter(setting);
		if (null != sqlLogFilter) {
			dbConfig.addSqlFilter(sqlLogFilter);
		}

		// 大小写等配置
		final String caseInsensitive = setting.getAndRemove(DSKeys.KEY_CASE_INSENSITIVE);
		if (StrUtil.isNotBlank(caseInsensitive)) {
			dbConfig.setCaseInsensitive(Convert.toBoolean(caseInsensitive));
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
	 * @return {@link SqlLogFilter}
	 */
	private static SqlLogFilter getSqlLogFilter(final Setting setting) {
		// 初始化SQL显示
		final boolean isShowSql = Convert.toBoolean(setting.remove(DSKeys.KEY_SHOW_SQL), false);
		if (!isShowSql) {
			return null;
		}

		final boolean isFormatSql = Convert.toBoolean(setting.remove(DSKeys.KEY_FORMAT_SQL), false);
		final boolean isShowParams = Convert.toBoolean(setting.remove(DSKeys.KEY_SHOW_PARAMS), false);
		String sqlLevelStr = setting.remove(DSKeys.KEY_SQL_LEVEL);
		if (null != sqlLevelStr) {
			sqlLevelStr = sqlLevelStr.toUpperCase();
		}
		final Level level = Convert.toEnum(Level.class, sqlLevelStr, Level.DEBUG);

		final SqlLog sqlLog = new SqlLog();
		sqlLog.init(isShowSql, isFormatSql, isShowParams, level);

		return new SqlLogFilter(sqlLog);
	}
}
