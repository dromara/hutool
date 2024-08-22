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

package org.dromara.hutool.http.server.filter;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;

/**
 * 默认异常处理拦截器
 *
 * @author looly
 */
public class DefaultExceptionFilter extends ExceptionFilter{

	private final static String TEMPLATE_ERROR = "<!DOCTYPE html><html><head><title>Hutool - Error report</title><style>h1,h3 {color:white; background-color: gray;}</style></head><body><h1>HTTP Status {} - {}</h1><hr size=\"1\" noshade=\"noshade\" /><p>{}</p><hr size=\"1\" noshade=\"noshade\" /><h3>Hutool</h3></body></html>";

	@Override
	public void afterException(final HttpServerRequest req, final HttpServerResponse res, final Throwable e) {
		String content = ExceptionUtil.stacktraceToString(e);
		content = content.replace("\n", "<br/>\n");
		content = StrUtil.format(TEMPLATE_ERROR, 500, req.getURI(), content);

		res.sendError(500, content);
	}
}
