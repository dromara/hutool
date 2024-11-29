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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.http.HttpException;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * 代理信息，提供代理选择器和代理验证
 *
 * @author looly
 * @since 6.0.0
 */
public class ProxyInfo {

	private String authHost;
	private int authPort;
	private ProxySelector proxySelector;
	private PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param host 代理地址
	 * @param port 代理端口
	 */
	public ProxyInfo(final String host, final int port) {
		this(new ProxySelector() {
			@Override
			public List<Proxy> select(final URI uri) {
				return ListUtil.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
			}

			@Override
			public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
				throw new HttpException(ioe);
			}
		}, null);
		this.authHost = host;
		this.authPort = port;
	}

	/**
	 * 构造
	 *
	 * @param proxySelector {@link ProxySelector}
	 * @param auth          用户名和密码
	 */
	public ProxyInfo(final ProxySelector proxySelector, final PasswordAuthentication auth) {
		this.proxySelector = proxySelector;
		this.auth = auth;
	}

	/**
	 * 获取代理验证主机
	 *
	 * @return 代理验证主机
	 */
	public String getAuthHost() {
		return authHost;
	}

	/**
	 * 设置代理验证主机
	 *
	 * @param authHost 代理验证主机
	 */
	public void setAuthHost(final String authHost) {
		this.authHost = authHost;
	}

	/**
	 * 获取代理验证端口
	 *
	 * @return 代理验证端口
	 */
	public int getAuthPort() {
		return authPort;
	}

	/**
	 * 设置代理验证端口
	 *
	 * @param authPort 代理验证端口
	 */
	public void setAuthPort(final int authPort) {
		this.authPort = authPort;
	}

	/**
	 * 获取代理选择器
	 *
	 * @return {@link ProxySelector}
	 */
	public ProxySelector getProxySelector() {
		return proxySelector;
	}

	/**
	 * 获取第一个代理<br>
	 * 如果代理选择器为空，返回null
	 *
	 * @param uri {@link URI}
	 * @return {@link Proxy}
	 */
	public Proxy selectFirst(final URI uri){
		if(null != proxySelector){
			final List<Proxy> select = proxySelector.select(uri);
			if(CollUtil.isNotEmpty(select)){
				return select.get(0);
			}
		}
		return null;
	}

	/**
	 * 设置代理选择器
	 *
	 * @param proxySelector {@link ProxySelector}
	 * @return this
	 */
	public ProxyInfo setProxySelector(final ProxySelector proxySelector) {
		this.proxySelector = proxySelector;
		return this;
	}

	/**
	 * 设置代理验证信息
	 *
	 * @param user 用户名
	 * @param pass 密码
	 * @return this
	 */
	public ProxyInfo setAuth(final String user, final char[] pass) {
		return setAuth(new PasswordAuthentication(user, pass));
	}

	/**
	 * 设置代理验证信息
	 *
	 * @param auth {@link PasswordAuthentication}
	 * @return this
	 */
	public ProxyInfo setAuth(final PasswordAuthentication auth) {
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
