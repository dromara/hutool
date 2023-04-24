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

package org.dromara.hutool.http.client.engine;

import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;

import java.io.Closeable;

/**
 * HTTP客户端引擎接口，通过不同实现，完成HTTP请求发送
 *
 * @author looly
 * @since 6.0.0
 */
public interface ClientEngine extends Closeable {

	/**
	 * 设置客户端引擎参数，如超时、代理等信息
	 *
	 * @param config 客户端设置
	 * @return this
	 */
	ClientEngine setConfig(ClientConfig config);

	/**
	 * 发送HTTP请求
	 *
	 * @param message HTTP请求消息
	 * @return 响应内容
	 */
	Response send(Request message);

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return 对应HTTP客户端实现的引擎对象
	 * @since 6.0.0
	 */
	Object getRawEngine();
}
