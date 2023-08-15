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

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.dialect.DriverUtil;

import java.util.Properties;

/**
 * 数据库配置
 *
 * @author Looly
 */
public class DbConfig {

	//-------------------------------------------------------------------- Fields start
	private String driver;        //数据库驱动
	private String url;            //jdbc url
	private String user;            //用户名
	private String pass;            //密码

	private int initialSize;        //初始连接数
	private int minIdle;            //最小闲置连接数
	private int maxActive;        //最大活跃连接数
	private long maxWait;        //获取连接的超时等待

	// 连接配置
	private Properties connProps;
	//-------------------------------------------------------------------- Fields end

	//-------------------------------------------------------------------- Constructor start
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
	//-------------------------------------------------------------------- Constructor end

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

	//-------------------------------------------------------------------- Getters and Setters start
	public String getDriver() {
		return driver;
	}

	public void setDriver(final String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(final String pass) {
		this.pass = pass;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(final int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(final int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(final int maxActive) {
		this.maxActive = maxActive;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(final long maxWait) {
		this.maxWait = maxWait;
	}

	public Properties getConnProps() {
		return connProps;
	}

	public void setConnProps(final Properties connProps) {
		this.connProps = connProps;
	}

	public void addConnProps(final String key, final String value){
		if(null == this.connProps){
			this.connProps = new Properties();
		}
		this.connProps.setProperty(key, value);
	}
	//-------------------------------------------------------------------- Getters and Setters end
}
