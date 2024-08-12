/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.http.server.filter;

import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;
import com.sun.net.httpserver.Filter;

import java.io.IOException;

/**
 * 过滤器接口，用于简化{@link Filter} 使用
 *
 * @author looly
 * @since 5.5.7
 */
@FunctionalInterface
public interface HttpFilter {

	/**
	 * 执行过滤
	 * @param req {@link HttpServerRequest} 请求对象，用于获取请求内容
	 * @param res {@link HttpServerResponse} 响应对象，用于写出内容
	 * @param chain {@link Filter.Chain}
	 * @throws IOException IO异常
	 */
	void doFilter(HttpServerRequest req, HttpServerResponse res, Filter.Chain chain) throws IOException;
}
