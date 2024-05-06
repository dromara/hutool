/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.http.server.filter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;

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
