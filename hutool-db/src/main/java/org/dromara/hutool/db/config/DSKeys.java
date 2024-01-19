/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
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

/**
 * 数据源配置的字段名
 *
 * @author Looly
 * @since 6.0.0
 */
public interface DSKeys {

	/**
	 * 配置文件中配置属性名：是否显示SQL
	 */
	String KEY_SHOW_SQL = "showSql";
	/**
	 * 配置文件中配置属性名：是否格式化SQL
	 */
	String KEY_FORMAT_SQL = "formatSql";
	/**
	 * 配置文件中配置属性名：是否显示参数
	 */
	String KEY_SHOW_PARAMS = "showParams";
	/**
	 * 配置文件中配置属性名：显示的日志级别
	 */
	String KEY_SQL_LEVEL = "sqlLevel";
	/**
	 * 配置文件中配置属性名：是否忽略大小写
	 */
	String KEY_CASE_INSENSITIVE = "caseInsensitive";

	/**
	 * 某些数据库需要的特殊配置项需要的配置项
	 */
	String[] KEY_CONN_PROPS = {"remarks", "useInformationSchema"};

	/**
	 * 别名字段名：URL
	 */
	String[] KEY_ALIAS_URL = {"url", "jdbcUrl"};
	/**
	 * 别名字段名：驱动名
	 */
	String[] KEY_ALIAS_DRIVER = {"driver", "driverClassName"};
	/**
	 * 别名字段名：用户名
	 */
	String[] KEY_ALIAS_USER = {"user", "username"};
	/**
	 * 别名字段名：密码
	 */
	String[] KEY_ALIAS_PASSWORD = {"pass", "password"};
}
