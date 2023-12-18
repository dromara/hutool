/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.ds;

import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.driver.DriverUtil;

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

	/**
	 * 创建DbConfig
	 *
	 * @return DbConfig
	 */
	public static DbConfig of() {
		return new DbConfig();
	}

	private String driver;        //数据库驱动
	private String url;            //jdbc url
	private String user;            //用户名
	private String pass;            //密码

	// 连接配置
	private Properties connProps;
	// 连接池配置
	private Properties poolProps;

	/**
	 * 构造
	 */
	public DbConfig() {
	}

	/**
	 * 构造
	 *
	 * @param url  jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public DbConfig(final String url, final String user, final String pass) {
		init(url, user, pass);
	}

	/**
	 * 初始化
	 *
	 * @param url  jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public void init(final String url, final String user, final String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.driver = DriverUtil.identifyDriver(url);
		try {
			Class.forName(this.driver);
		} catch (final ClassNotFoundException e) {
			throw new DbRuntimeException(e, "Get jdbc driver from [{}] error!", url);
		}
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
}
