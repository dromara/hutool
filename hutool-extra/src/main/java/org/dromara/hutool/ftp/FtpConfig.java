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

package org.dromara.hutool.ftp;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * FTP配置项，提供FTP各种参数信息
 *
 * @author looly
 */
public class FtpConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	public static FtpConfig of() {
		return new FtpConfig();
	}

	/**
	 * 主机
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 编码
	 */
	private Charset charset;

	/**
	 * 连接超时时长，单位毫秒
	 */
	private long connectionTimeout;

	/**
	 * Socket连接超时时长，单位毫秒
	 */
	private long soTimeout;

	/**
	 * 设置服务器语言
	 */
	private String serverLanguageCode;

	/**
	 * 设置服务器系统关键词
	 */
	private String systemKey;

	/**
	 * 构造
	 */
	public FtpConfig() {
	}

	/**
	 * 构造
	 *
	 * @param host               主机
	 * @param port               端口
	 * @param user               用户名
	 * @param password           密码
	 * @param charset            编码
	 */
	public FtpConfig(final String host, final int port, final String user, final String password, final Charset charset) {
		this(host, port, user, password, charset, null, null);
	}

	/**
	 * 构造
	 *
	 * @param host               主机
	 * @param port               端口
	 * @param user               用户名
	 * @param password           密码
	 * @param charset            编码
	 * @param serverLanguageCode 服务器语言
	 * @param systemKey          系统关键字
	 * @since 5.5.7
	 */
	public FtpConfig(final String host, final int port, final String user, final String password, final Charset charset, final String serverLanguageCode, final String systemKey) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.charset = charset;
		this.serverLanguageCode = serverLanguageCode;
		this.systemKey = systemKey;
	}

	public String getHost() {
		return host;
	}

	public FtpConfig setHost(final String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public FtpConfig setPort(final int port) {
		this.port = port;
		return this;
	}

	public String getUser() {
		return user;
	}

	public FtpConfig setUser(final String user) {
		this.user = user;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public FtpConfig setPassword(final String password) {
		this.password = password;
		return this;
	}

	public Charset getCharset() {
		return charset;
	}

	public FtpConfig setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public FtpConfig setConnectionTimeout(final long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public long getSoTimeout() {
		return soTimeout;
	}

	public FtpConfig setSoTimeout(final long soTimeout) {
		this.soTimeout = soTimeout;
		return this;
	}

	public String getServerLanguageCode() {
		return serverLanguageCode;
	}

	public FtpConfig setServerLanguageCode(final String serverLanguageCode) {
		this.serverLanguageCode = serverLanguageCode;
		return this;
	}

	public String getSystemKey() {
		return systemKey;
	}

	public FtpConfig setSystemKey(final String systemKey) {
		this.systemKey = systemKey;
		return this;
	}
}
