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

package org.dromara.hutool.core.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 账号密码形式的{@link Authenticator} 实现。
 *
 * @author looly
 * @since 5.5.3
 */
public class UserPassAuthenticator extends Authenticator {

	/**
	 * 创建账号密码形式的{@link Authenticator} 实现。
	 *
	 * @param user 用户名
	 * @param pass 密码
	 * @return PassAuth
	 */
	public static UserPassAuthenticator of(final String user, final char[] pass) {
		return new UserPassAuthenticator(user, pass);
	}

	private final PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param user 用户名
	 * @param pass 密码
	 */
	public UserPassAuthenticator(final String user, final char[] pass) {
		auth = new PasswordAuthentication(user, pass);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return auth;
	}
}
