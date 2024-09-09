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

package org.dromara.hutool.http.client.cookie;

import java.time.Instant;

/**
 * Cookie SPI接口，用于自定义Cookie的实现<br>
 * 遵循RFC6265规范：https://datatracker.ietf.org/doc/html/rfc6265<br>
 * 参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Cookies<br>
 * Cookie 主要用于以下三个方面：
 * <ul>
 *     <li>会话状态管理：    用户登录状态、购物车、游戏分数或其他需要记录的信息</li>
 *     <li>个性化设置：     如用户自定义设置、主题和其他设置</li>
 *     <li>浏览器行为跟踪： 如跟踪分析用户行为等</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public interface CookieSpi {

	/**
	 * 获取Cookie名称
	 *
	 * @return Cookie名称
	 */
	String getName();

	/**
	 * 获取Cookie值
	 *
	 * @return Cookie值
	 */
	String getValue();

	/**
	 * 限制访问 Cookie<br>
	 * 标记为 Secure 的 Cookie 只应通过被 HTTPS 协议加密过的请求发送给服务端<br>
	 *
	 * @return 是否限制访问 Cookie
	 */
	boolean isSecure();

	/**
	 * 限制 Cookie 的作用域<br>
	 * 标记为 HttpOnly 的 Cookie 只能在 HTTP 协议中访问，不能通过脚本语言（如 JavaScript）访问<br>
	 *
	 * @return 是否限制 Cookie 的作用域
	 */
	boolean isHttpOnly();

	/**
	 * 限制 Cookie 主机作用域，一般包含子域名<br>
	 * Cookie 作用域，默认为空，表示所有域名下生效<br>
	 *
	 * @return Cookie 作用域
	 */
	boolean isHostOnly();

	/**
	 * 限制 Cookie 主机作用域，一般包含子域名<br>
	 * Cookie 作用域，默认为空，表示所有域名下生效<br>
	 *
	 * @return Cookie 作用域
	 */
	String getDomain();

	/**
	 * 限制 Cookie URL路径作用域，该 URL 路径必须存在于请求的 URL 中，子路径也会匹配<br>
	 * Cookie 作用域，默认为空，表示所有路径下生效<br>
	 *
	 * @return Cookie 作用域
	 */
	String getPath();

	/**
	 * Cookie是否过期
	 *
	 * @param now 当前时间，用于判断是否过期
	 * @return 是否过期
	 */
	boolean isExpired(Instant now);

	/**
	 * Cookie是否过期
	 *
	 * @return 是否过期
	 */
	default boolean isExpired() {
		return isExpired(Instant.now());
	}

	/**
	 * 自定义属性，用于扩展
	 *
	 * @param name 属性名
	 * @return 属性值
	 */
	String getAttribute(String name);

	/**
	 * 是否持久化，即Cookie是否在Session关闭前一直有效
	 *
	 * @return 是否持久化
	 */
	boolean isPersistent();
}
