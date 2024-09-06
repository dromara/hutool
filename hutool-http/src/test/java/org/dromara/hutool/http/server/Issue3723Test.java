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

import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.meta.ContentType;

public class Issue3723Test {
	public static void main(final String[] args) {
		final SimpleServer server = HttpUtil.createServer(8888);
		server.addFilter((req, res, chain) -> {
			final String requestId = IdUtil.fastSimpleUUID();
			req.getHttpExchange().setAttribute("requestId", requestId);
			res.addHeader("X-Request-Id", requestId);

			res.write("new Content");

			chain.doFilter(req.getHttpExchange());
		});
		server.addAction("/", (req, res)-> res.write("Hello Hutool Server", ContentType.JSON.getValue()));
		server.start();
	}
}
