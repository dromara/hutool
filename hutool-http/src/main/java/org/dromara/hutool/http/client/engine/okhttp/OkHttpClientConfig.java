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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.ConnectionPool;
import org.dromara.hutool.http.client.ClientConfig;

/**
 * OkHttpClient 配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class OkHttpClientConfig extends ClientConfig {
	/**
	 * 创建新的 OkHttpClientConfig
	 *
	 * @return OkHttpClientConfig
	 */
	public static OkHttpClientConfig of() {
		return new OkHttpClientConfig();
	}

	private ConnectionPool connectionPool;

	/**
	 * 获取连接池
	 *
	 * @return ConnectionPool
	 */
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	/**
	 * 设置连接池
	 *
	 * @param connectionPool ConnectionPool
	 * @return this
	 */
	public OkHttpClientConfig setConnectionPool(final ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
		return this;
	}
}
