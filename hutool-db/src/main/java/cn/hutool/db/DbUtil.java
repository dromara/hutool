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

package cn.hutool.db;

import cn.hutool.core.convert.Convert;
import cn.hutool.db.ds.DSKeys;
import cn.hutool.log.Log;
import cn.hutool.log.level.Level;
import cn.hutool.setting.Setting;

/**
 * 数据库操作工具类
 *
 * @author Luxiaolei
 */
public final class DbUtil {
	private final static Log log = Log.get();

	/**
	 * 从配置文件中读取SQL打印选项，读取后会去除相应属性
	 *
	 * @param setting 配置文件
	 * @since 4.1.7
	 */
	public static void setShowSqlGlobal(final Setting setting) {
		// 初始化SQL显示
		final boolean isShowSql = Convert.toBoolean(setting.remove(DSKeys.KEY_SHOW_SQL), false);
		final boolean isFormatSql = Convert.toBoolean(setting.remove(DSKeys.KEY_FORMAT_SQL), false);
		final boolean isShowParams = Convert.toBoolean(setting.remove(DSKeys.KEY_SHOW_PARAMS), false);
		String sqlLevelStr = setting.remove(DSKeys.KEY_SQL_LEVEL);
		if (null != sqlLevelStr) {
			sqlLevelStr = sqlLevelStr.toUpperCase();
		}
		final Level level = Convert.toEnum(Level.class, sqlLevelStr, Level.DEBUG);
		log.debug("Show sql: [{}], format sql: [{}], show params: [{}], level: [{}]", isShowSql, isFormatSql, isShowParams, level);
		setShowSqlGlobal(isShowSql, isFormatSql, isShowParams, level);
	}

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 *
	 * @param isShowSql    是否显示SQL
	 * @param isFormatSql  是否格式化显示的SQL
	 * @param isShowParams 是否打印参数
	 * @param level        SQL打印到的日志等级
	 * @see GlobalDbConfig#setShowSql(boolean, boolean, boolean, Level)
	 * @since 4.1.7
	 */
	public static void setShowSqlGlobal(final boolean isShowSql, final boolean isFormatSql, final boolean isShowParams, final Level level) {
		GlobalDbConfig.setShowSql(isShowSql, isFormatSql, isShowParams, level);
	}

	/**
	 * 设置全局是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param caseInsensitive 否在结果中忽略大小写
	 * @see GlobalDbConfig#setCaseInsensitive(boolean)
	 * @since 5.2.4
	 */
	public static void setCaseInsensitiveGlobal(final boolean caseInsensitive) {
		GlobalDbConfig.setCaseInsensitive(caseInsensitive);
	}

	/**
	 * 设置全局是否INSERT语句中默认返回主键（默认返回主键）<br>
	 * 如果false，则在Insert操作后，返回影响行数
	 * 主要用于某些数据库不支持返回主键的情况
	 *
	 * @param returnGeneratedKey 是否INSERT语句中默认返回主键
	 * @see GlobalDbConfig#setReturnGeneratedKey(boolean)
	 * @since 5.3.10
	 */
	public static void setReturnGeneratedKeyGlobal(final boolean returnGeneratedKey) {
		GlobalDbConfig.setReturnGeneratedKey(returnGeneratedKey);
	}

	/**
	 * 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 *
	 * @param dbSettingPath 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 * @see GlobalDbConfig#setDbSettingPath(String)
	 * @since 5.8.0
	 */
	public static void setDbSettingPathGlobal(final String dbSettingPath) {
		GlobalDbConfig.setDbSettingPath(dbSettingPath);
	}
}
