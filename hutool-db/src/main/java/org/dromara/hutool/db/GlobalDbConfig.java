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

package org.dromara.hutool.db;

import org.dromara.hutool.core.io.resource.NoResourceException;
import org.dromara.hutool.db.sql.SqlLog;
import org.dromara.hutool.log.level.Level;
import org.dromara.hutool.setting.Setting;

/**
 * DB全局配置配置项
 *
 * @author looly
 * @since 5.3.10
 */
public class GlobalDbConfig {
	/**
	 * 数据库配置文件可选路径1
	 */
	private static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	/**
	 * 数据库配置文件可选路径2
	 */
	private static final String DEFAULT_DB_SETTING_PATH2 = "db.setting";

	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected static boolean caseInsensitive = true;
	/**
	 * 是否INSERT语句中默认返回主键（默认返回主键）
	 */
	protected static boolean returnGeneratedKey = true;
	/**
	 * 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 *
	 * @since 5.8.0
	 */
	private static String dbSettingPath = null;

	/**
	 * 设置全局是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param isCaseInsensitive 否在结果中忽略大小写
	 */
	public static void setCaseInsensitive(final boolean isCaseInsensitive) {
		caseInsensitive = isCaseInsensitive;
	}

	/**
	 * 设置全局是否INSERT语句中默认返回主键（默认返回主键）<br>
	 * 如果false，则在Insert操作后，返回影响行数
	 * 主要用于某些数据库不支持返回主键的情况
	 *
	 * @param isReturnGeneratedKey 是否INSERT语句中默认返回主键
	 */
	public static void setReturnGeneratedKey(final boolean isReturnGeneratedKey) {
		returnGeneratedKey = isReturnGeneratedKey;
	}

	/**
	 * 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 *
	 * @param customDbSettingPath 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 * @since 5.8.0
	 */
	public static void setDbSettingPath(final String customDbSettingPath) {
		dbSettingPath = customDbSettingPath;
	}

	/**
	 * 获取自定义或默认位置数据库配置{@link Setting}
	 *
	 * @return 数据库配置
	 * @since 5.8.0
	 */
	public static Setting createDbSetting() {
		Setting setting;
		if (null != dbSettingPath) {
			// 自定义数据库配置文件位置
			try {
				setting = new Setting(dbSettingPath, false);
			} catch (final NoResourceException e3) {
				throw new NoResourceException("Customize db setting file [{}] not found !", dbSettingPath);
			}
		} else {
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
		}
		return setting;
	}

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 *
	 * @param isShowSql    是否显示SQL，{@code null}表示保持默认
	 * @param isFormatSql  是否格式化显示的SQL，{@code null}表示保持默认
	 * @param isShowParams 是否打印参数，{@code null}表示保持默认
	 * @param level        日志级别，{@code null}表示保持默认
	 */
	public static void setShowSql(final Boolean isShowSql, final Boolean isFormatSql, final Boolean isShowParams, final Level level) {
		SqlLog.INSTANCE.init(isShowSql, isFormatSql, isShowParams, level);
	}
}
