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

import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.db.sql.filter.SqlFilter;
import org.dromara.hutool.db.sql.filter.SqlFilterChain;

import java.util.Properties;

/**
 * 数据库配置，包括：
 * <ul>
 *     <li>基本配置项，如driver、url、user、password等</li>
 *     <li>连接配置，如remarks、useInformationSchema等</li>
 *     <li>连接池配置，如初始容量、最大容量等，取决于连接池库具体要求</li>
 * </ul>
 *
 * @author Looly
 */
public class DbConfig {

	// region ----- of
	/**
	 * 创建DsConfig
	 *
	 * @param url  jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 * @return DsConfig
	 */
	public static DbConfig of(final String url, final String user, final String pass) {
		return of().setUrl(url).setUser(user).setPass(pass).setDriver(DriverUtil.identifyDriver(url));
	}

	/**
	 * 创建DsConfig
	 *
	 * @return DsConfig
	 */
	public static DbConfig of() {
		return new DbConfig();
	}
	// endregion

	private String driver;        //数据库驱动
	private String url;            //jdbc url
	private String user;            //用户名
	private String pass;            //密码

	// 连接配置
	private Properties connProps;
	// 连接池配置
	private Properties poolProps;

	// 其它配置
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	private boolean caseInsensitive = true;
	/**
	 * 是否INSERT语句中默认返回主键（默认返回主键）
	 */
	private boolean returnGeneratedKey = true;

	/**
	 * SQL过滤器，用于在生成SQL前对SQL做操作，如记录日志等
	 */
	private SqlFilterChain sqlFilters;

	/**
	 * 构造
	 */
	public DbConfig() {
	}

	/**
	 * 获取JDBC驱动
	 *
	 * @return JDBC驱动
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * 设置JDBC驱动
	 *
	 * @param driver JDBC驱动
	 * @return this
	 */
	public DbConfig setDriver(final String driver) {
		this.driver = driver;
		return this;
	}

	/**
	 * 获取JDBC URL
	 *
	 * @return JDBC URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置JDBC URL
	 *
	 * @param url JDBC URL
	 * @return this
	 */
	public DbConfig setUrl(final String url) {
		this.url = url;
		return this;
	}

	/**
	 * 获取用户名
	 *
	 * @return 用户名
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 设置用户名
	 *
	 * @param user 用户名
	 * @return this
	 */
	public DbConfig setUser(final String user) {
		this.user = user;
		return this;
	}

	/**
	 * 获取密码
	 *
	 * @return 密码
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * 设置密码
	 *
	 * @param pass 密码
	 * @return this
	 */
	public DbConfig setPass(final String pass) {
		this.pass = pass;
		return this;
	}

	/**
	 * 获取连接属性
	 *
	 * @return 连接属性
	 */
	public Properties getConnProps() {
		return connProps;
	}

	/**
	 * 设置连接属性
	 *
	 * @param connProps 连接属性
	 * @return this
	 */
	public DbConfig setConnProps(final Properties connProps) {
		this.connProps = connProps;

		return this;
	}

	/**
	 * 增加连接属性
	 *
	 * @param key   属性名
	 * @param value 属性值
	 * @return this
	 */
	public DbConfig addConnProps(final String key, final String value) {
		if (null == this.connProps) {
			this.connProps = new Properties();
		}
		this.connProps.setProperty(key, value);
		return this;
	}

	/**
	 * 获取连接池属性
	 *
	 * @return 连接池属性
	 */
	public Properties getPoolProps() {
		return poolProps;
	}

	/**
	 * 设置连接池属性
	 *
	 * @param poolProps 连接池属性
	 * @return this
	 */
	public DbConfig setPoolProps(final Properties poolProps) {
		this.poolProps = poolProps;

		return this;
	}

	/**
	 * 增加连接池属性
	 *
	 * @param key   属性名
	 * @param value 属性值
	 * @return this
	 */
	public DbConfig addPoolProps(final String key, final String value) {
		if (null == this.poolProps) {
			this.poolProps = new Properties();
		}
		this.poolProps.setProperty(key, value);
		return this;
	}

	/**
	 * 获取是否在结果中忽略大小写
	 *
	 * @return 是否在结果中忽略大小写
	 */
	public boolean isCaseInsensitive() {
		return this.caseInsensitive;
	}

	/**
	 * 设置是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param isCaseInsensitive 是否在结果中忽略大小写
	 * @return this
	 */
	public DbConfig setCaseInsensitive(final boolean isCaseInsensitive) {
		this.caseInsensitive = isCaseInsensitive;
		return this;
	}

	/**
	 * INSERT语句中是否返回主键
	 *
	 * @return 是否返回主键
	 */
	public boolean isReturnGeneratedKey() {
		return this.returnGeneratedKey;
	}

	/**
	 * 设置是否INSERT语句中默认返回主键（默认返回主键）<br>
	 * 如果false，则在Insert操作后，返回影响行数
	 * 主要用于某些数据库不支持返回主键的情况
	 *
	 * @param isReturnGeneratedKey 是否INSERT语句中默认返回主键
	 * @return this
	 */
	public DbConfig setReturnGeneratedKey(final boolean isReturnGeneratedKey) {
		returnGeneratedKey = isReturnGeneratedKey;
		return this;
	}

	/**
	 * 增加SQL过滤器
	 *
	 * @param filter SQL过滤器
	 * @return this
	 */
	public DbConfig addSqlFilter(final SqlFilter filter){
		if(null == this.sqlFilters){
			this.sqlFilters = new SqlFilterChain();
		}
		this.sqlFilters.addChain(filter);
		return this;
	}
}
