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

package org.dromara.hutool.extra.ssh;

/**
 * 连接者对象，提供一些连接的基本信息，包括：
 * <ul>
 *     <li>host：主机名</li>
 *     <li>port：端口</li>
 *     <li>user：用户名（默认root）</li>
 *     <li>password：密码</li>
 *     <li>timeout：连接超时毫秒数</li>
 * </ul>
 *
 * @author looly
 */
public class Connector {

	/**
	 * 创建Connector，所有参数为默认，用于构建模式
	 *
	 * @return Connector
	 */
	public static Connector of() {
		return new Connector();
	}

	/**
	 * 创建Connector
	 *
	 * @param host     主机名
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @return Connector
	 */
	public static Connector of(final String host, final int port, final String user, final String password) {
		return of(host, port, user, password, 0);
	}

	/**
	 * 创建Connector
	 *
	 * @param host     主机名
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @param timeout  连接超时时长，0表示默认
	 * @return Connector
	 */
	public static Connector of(final String host, final int port, final String user, final String password, final long timeout) {
		return new Connector(host, port, user, password, timeout);
	}

	private String host;
	private int port;
	private String user = "root";
	private String password;
	private long timeout;

	/**
	 * 构造
	 */
	public Connector() {
	}

	/**
	 * 构造
	 *
	 * @param host     主机名
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @param timeout  连接超时时长，0表示默认
	 */
	public Connector(final String host, final int port, final String user, final String password, final long timeout) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.timeout = timeout;
	}

	/**
	 * 获得主机名
	 *
	 * @return 主机名
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设定主机名
	 *
	 * @param host 主机名
	 * @return this
	 */
	public Connector setHost(final String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获得端口号
	 *
	 * @return 端口号
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设定端口号
	 *
	 * @param port 端口号
	 * @return this
	 */
	public Connector setPort(final int port) {
		this.port = port;
		return this;
	}

	/**
	 * 获得用户名
	 *
	 * @return 用户名
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 设定用户名
	 *
	 * @param name 用户名
	 * @return this
	 */
	public Connector setUser(final String name) {
		this.user = name;
		return this;
	}

	/**
	 * 获得密码
	 *
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设定密码
	 *
	 * @param password 密码
	 * @return this
	 */
	public Connector setPassword(final String password) {
		this.password = password;
		return this;
	}

	/**
	 * 获得连接超时时间
	 *
	 * @return 连接超时时间
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置连接超时时间
	 *
	 * @param timeout 连接超时时间
	 * @return this
	 */
	public Connector setTimeout(final long timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * toString方法仅用于测试显示
	 */
	@Override
	public String toString() {
		return "Connector{" +
			"host='" + host + '\'' +
			", port=" + port +
			", user='" + user + '\'' +
			", password='" + password + '\'' +
			", timeout=" + timeout +
			'}';
	}
}
