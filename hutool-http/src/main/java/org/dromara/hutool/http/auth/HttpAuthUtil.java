/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
