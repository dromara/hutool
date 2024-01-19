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

import java.util.Properties;

/**
 * 数据库连接配置，提供包括jdbcUrl、用户名和密码等信息
 *
 * @param <T> 返回值类型，用于继承后链式调用
 */
@SuppressWarnings("unchecked")
public class ConnectionConfig<T extends ConnectionConfig<?>> {

	private String driver;        //数据库驱动
	private String url;            //jdbc url
	private String user;            //用户名
	private String pass;            //密码

	/**
	 * 连接配置
	 */
	private Properties connProps;
	/**
	 * 连接池配置
	 */
	private Properties poolProps;

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
	public T setDriver(final String driver) {
		this.driver = driver;
		return (T) this;
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
	public T setUrl(final String url) {
		this.url = url;
		return (T) this;
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
	public T setUser(final String user) {
		this.user = user;
		return (T) this;
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
	public T setPass(final String pass) {
		this.pass = pass;
		return (T) this;
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
	public T setConnProps(final Properties connProps) {
		this.connProps = connProps;
		return (T) this;
	}

	/**
	 * 增加连接属性
	 *
	 * @param key   属性名
	 * @param value 属性值
	 * @return this
	 */
	public T addConnProps(final String key, final String value) {
		if (null == this.connProps) {
			this.connProps = new Properties();
		}
		this.connProps.setProperty(key, value);
		return (T) this;
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
	public T setPoolProps(final Properties poolProps) {
		this.poolProps = poolProps;

		return (T) this;
	}

	/**
	 * 增加连接池属性
	 *
	 * @param key   属性名
	 * @param value 属性值
	 * @return this
	 */
	public T addPoolProps(final String key, final String value) {
		if (null == this.poolProps) {
			this.poolProps = new Properties();
		}
		this.poolProps.setProperty(key, value);
		return (T) this;
	}
}
