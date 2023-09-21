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

package org.dromara.hutool.http.server;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.meta.HeaderName;

public class RedirectServerTest {
	public static void main(final String[] args) {
		HttpUtil.createServer(8888).addAction("/redirect1", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect2");
			response.addHeader(HeaderName.SET_COOKIE.getValue(),"redirect1=1; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect2", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect3");
			response.addHeader(HeaderName.SET_COOKIE.getValue(), "redirect2=2; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect3", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect4");
			response.addHeader(HeaderName.SET_COOKIE.getValue(),"redirect3=3; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect4", (request, response) -> {
			final String cookie = request.getHeader(HeaderName.COOKIE);
			Console.log(cookie);
			response.sendOk();
		}).start();
	}
}
