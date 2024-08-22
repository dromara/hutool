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

package org.dromara.hutool.http.server.action;

import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;

import java.io.IOException;

/**
 * 请求处理接口<br>
 * 当用户请求某个Path，则调用相应Action的doAction方法
 *
 * @author Looly
 * @since 5.2.6
 */
@FunctionalInterface
public interface Action {

	/**
	 * 处理请求
	 *
	 * @param request  请求对象
	 * @param response 响应对象
	 * @throws IOException IO异常
	 */
	void doAction(HttpServerRequest request, HttpServerResponse response) throws IOException;
}
