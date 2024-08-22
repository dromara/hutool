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

package org.dromara.hutool.http.auth;

import org.dromara.hutool.core.codec.binary.Base64;

import java.net.PasswordAuthentication;
import java.nio.charset.Charset;

/**
 * HTTP验证工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpAuthUtil {

	/**
	 * 构建简单的账号秘密验证信息，构建后类似于：
	 * <pre>
	 *     Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param authentication {@link PasswordAuthentication}
	 * @param charset        编码（如果账号或密码中有非ASCII字符适用）
	 * @return 密码验证信息
	 */
	public static String buildBasicAuth(final PasswordAuthentication authentication, final Charset charset) {
		return buildBasicAuth(authentication.getUserName(), String.valueOf(authentication.getPassword()), charset);
	}

	/**
	 * 构建简单的账号秘密验证信息，构建后类似于：
	 * <pre>
	 *     Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 账号
	 * @param password 密码
	 * @param charset  编码（如果账号或密码中有非ASCII字符适用）
	 * @return 密码验证信息
	 */
	public static String buildBasicAuth(final String username, final String password, final Charset charset) {
		final String data = username.concat(":").concat(password);
		return "Basic " + Base64.encode(data, charset);
	}
}
