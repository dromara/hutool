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

package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.handler.HttpHandler;

/**
 * HTTP服务器引擎
 *
 * @author looly
 * @since 6.0.0
 */
public interface ServerEngine {

	/**
	 * 初始化HTTP服务器
	 *
	 * @param config 配置项
	 * @return this
	 */
	ServerEngine init(ServerConfig config);

	/**
	 * 设置请求处理器
	 *
	 * @param handler 请求处理器
	 * @return this
	 */
	ServerEngine setHandler(HttpHandler handler);

	/**
	 * 启动HTTP服务器
	 */
	void start();

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return 对应HTTP服务器实现的引擎对象
	 * @since 6.0.0
	 */
	Object getRawEngine();
}
