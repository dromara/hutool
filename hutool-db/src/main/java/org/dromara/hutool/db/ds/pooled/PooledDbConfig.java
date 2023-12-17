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

import org.dromara.hutool.db.ds.simple.DbConfig;

/**
 * 数据库配置
 *
 * @author Looly
 */
public class PooledDbConfig extends DbConfig {

	private int initialSize;        //初始连接数
	private int minIdle;            //最小闲置连接数
	private int maxActive;        //最大活跃连接数
	private long maxWait;        //获取连接的超时等待

	/**
	 * 构造
	 */
	public PooledDbConfig() {
	}

	/**
	 * 构造
	 *
	 * @param url  jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public PooledDbConfig(final String url, final String user, final String pass) {
		super(url, user, pass);
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
}
