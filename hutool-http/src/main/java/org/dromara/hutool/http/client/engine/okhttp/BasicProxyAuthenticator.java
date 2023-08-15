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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.*;
import org.dromara.hutool.http.meta.HeaderName;

import java.net.PasswordAuthentication;

/**
 * 账号密码形式的代理验证<br>
 * 生成类似：
 * <pre>
 *     Proxy-Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class BasicProxyAuthenticator implements Authenticator {

	private final PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param passwordAuthentication 账号密码对
	 */
	public BasicProxyAuthenticator(final PasswordAuthentication passwordAuthentication) {
		auth = passwordAuthentication;
	}

	@Override
	public Request authenticate(final Route route, final Response response) {
		final String credential = Credentials.basic(
			auth.getUserName(),
			String.valueOf(auth.getPassword()));
		return response.request().newBuilder()
			.addHeader(HeaderName.PROXY_AUTHORIZATION.getValue(), credential)
			.build();
	}
}
