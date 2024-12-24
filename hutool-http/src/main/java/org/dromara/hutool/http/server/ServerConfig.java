/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.server;

import org.dromara.hutool.core.net.ssl.SSLContextUtil;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

/**
 * 服务器配置
 *
 * @author Looly
 */
public class ServerConfig {

	/**
	 * 创建配置
	 *
	 * @return 配置
	 */
	public static ServerConfig of() {
		return new ServerConfig();
	}

	private String host = "localhost";
	private int port = 8888;
	private String root;
	private SSLContext sslContext;

	private int maxHeaderSize;
	private long maxBodySize;
	private int coreThreads;
	private int maxThreads;
	private long idleTimeout;


	/**
	 * 获取服务器地址，默认127.0.0.1
	 *
	 * @return 服务器地址
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设置服务器地址，默认127.0.0.1
	 *
	 * @param host 服务器地址
	 * @return this
	 */
	public ServerConfig setHost(final String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获取服务器端口
	 *
	 * @return 服务器端口
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置服务器端口
	 *
	 * @param port 服务器端口
	 * @return this
	 */
	public ServerConfig setPort(final int port) {
		this.port = port;
		return this;
	}

	/**
	 * 获取服务器根目录
	 *
	 * @return 服务器根目录
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * 设置服务器根目录
	 *
	 * @param root 服务器根目录
	 * @return this
	 */
	public ServerConfig setRoot(final String root) {
		this.root = root;
		return this;
	}

	/**
	 * 获取SSL上下文
	 *
	 * @return SSL上下文
	 */
	public SSLContext getSslContext() {
		return sslContext;
	}

	/**
	 * 设置证书库<br>
	 * 此方法和{@link #setSslContext(SSLContext)}互斥
	 *
	 * @param keyStore 证书库
	 * @param passwd 密码
	 * @return this
	 */
	public ServerConfig setKeystore(final KeyStore keyStore, final char[] passwd) {
		return setSslContext(SSLContextUtil.createSSLContext(keyStore, passwd));
	}

	/**
	 * 设置SSL上下文
	 *
	 * @param sslContext SSL上下文
	 * @return this
	 */
	public ServerConfig setSslContext(final SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	/**
	 * 获取最大请求（响应）头大小
	 *
	 * @return maxHeaderSize 最大请求（响应）头大小
	 */
	public int getMaxHeaderSize() {
		return maxHeaderSize;
	}

	/**
	 * 设置最大请求（响应）头大小
	 *
	 * @param maxHeaderSize 最大请求（响应）头大小
	 * @return this
	 */
	public ServerConfig setMaxHeaderSize(final int maxHeaderSize) {
		this.maxHeaderSize = maxHeaderSize;
		return this;
	}

	/**
	 * 获取最大消息体大小
	 *
	 * @return maxBodySize 最大消息体大小
	 */
	public long getMaxBodySize() {
		return maxBodySize;
	}

	/**
	 * 设置最大消息体大小
	 *
	 * @param maxBodySize 最大消息体大小
	 * @return this
	 */
	public ServerConfig setMaxBodySize(final long maxBodySize) {
		this.maxBodySize = maxBodySize;
		return this;
	}

	/**
	 * 获取核心线程数
	 *
	 * @return coreThreads 核心线程数
	 */
	public int getCoreThreads() {
		return coreThreads;
	}

	/**
	 * 设置核心线程数
	 *
	 * @param coreThreads 核心线程数
	 * @return this
	 */
	public ServerConfig setCoreThreads(final int coreThreads) {
		this.coreThreads = coreThreads;
		return this;
	}

	/**
	 * 获取最大线程数
	 *
	 * @return maxThreads 最大线程数
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * 设置最大线程数
	 *
	 * @param maxThreads 最大线程数
	 * @return this
	 */
	public ServerConfig setMaxThreads(final int maxThreads) {
		this.maxThreads = maxThreads;
		return this;
	}

	/**
	 * 获取空闲超时时间
	 *
	 * @return idleTimeout 空闲超时时间
	 */
	public long getIdleTimeout() {
		return idleTimeout;
	}

	/**
	 * 设置空闲超时时间
	 *
	 * @param idleTimeout 空闲超时时间
	 * @return this
	 */
	public ServerConfig setIdleTimeout(final long idleTimeout) {
		this.idleTimeout = idleTimeout;
		return this;
	}
}
