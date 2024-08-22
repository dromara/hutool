/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.proxy;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * HTTP代理，提供代理地址和代理端口的持有。
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpProxy extends Proxy {

	private final String host;
	private final int port;
	private PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param host 域名或IP
	 * @param port 端口
	 */
	public HttpProxy(final String host, final int port) {
		super(Type.HTTP, new InetSocketAddress(host, port));
		this.host = host;
		this.port = port;
	}

	/**
	 * 获取域名或IP
	 *
	 * @return 域名或IP
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 获取端口
	 *
	 * @return 端口
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置代理验证信息
	 *
	 * @param user 用户名
	 * @param pass 密码
	 * @return this
	 */
	public HttpProxy setAuth(final String user, final char[] pass) {
		return setAuth(new PasswordAuthentication(user, pass));
	}

	/**
	 * 设置代理验证信息
	 *
	 * @param auth {@link PasswordAuthentication}
	 * @return this
	 */
	public HttpProxy setAuth(final PasswordAuthentication auth) {
		this.auth = auth;
		return this;
	}

	/**
	 * 获取代理验证信息
	 *
	 * @return {@link PasswordAuthentication}
	 */
	public PasswordAuthentication getAuth() {
		return this.auth;
	}
}
