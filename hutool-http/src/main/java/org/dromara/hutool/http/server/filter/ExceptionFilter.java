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

package org.dromara.hutool.http.server.filter;

import com.sun.net.httpserver.Filter;
import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;

/**
 * 异常处理过滤器
 *
 * @author looly
 */
public abstract class ExceptionFilter implements HttpFilter {

	@Override
	public void doFilter(final HttpServerRequest req, final HttpServerResponse res, final Filter.Chain chain) {
		try {
			chain.doFilter(req.getHttpExchange());
		} catch (final Throwable e) {
			afterException(req, res, e);
		}
	}

	/**
	 * 异常之后的处理逻辑
	 *
	 * @param req {@link HttpServerRequest}
	 * @param res {@link HttpServerResponse}
	 * @param e   异常
	 */
	public abstract void afterException(final HttpServerRequest req, final HttpServerResponse res, final Throwable e);
}
