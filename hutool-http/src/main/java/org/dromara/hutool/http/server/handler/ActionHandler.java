/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.server.handler;

import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;
import org.dromara.hutool.http.server.action.Action;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Action处理器，用于将HttpHandler转换为Action形式
 *
 * @author looly
 * @since 5.2.6
 */
public class ActionHandler implements HttpHandler {

	private final Action action;

	/**
	 * 构造
	 *
	 * @param action Action
	 */
	public ActionHandler(final Action action) {
		this.action = action;
	}

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		action.doAction(
				new HttpServerRequest(httpExchange),
				new HttpServerResponse(httpExchange)
		);
		httpExchange.close();
	}
}
