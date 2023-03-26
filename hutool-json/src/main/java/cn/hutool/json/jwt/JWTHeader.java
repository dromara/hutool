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

package cn.hutool.json.jwt;

import java.util.Map;

/**
 * JWT头部信息
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTHeader extends Claims {
	private static final long serialVersionUID = 1L;

	//Header names
	/**
	 * 加密算法，通常为HMAC SHA256（HS256）
	 */
	public static String ALGORITHM = "alg";
	/**
	 * 声明类型，一般为jwt
	 */
	public static String TYPE = "typ";
	/**
	 * 内容类型（content type）
	 */
	public static String CONTENT_TYPE = "cty";
	/**
	 * jwk的ID编号
	 */
	public static String KEY_ID = "kid";

	/**
	 * 增加“alg”头信息
	 *
	 * @param algorithm 算法ID，如HS265
	 * @return this
	 */
	public JWTHeader setAlgorithm(final String algorithm) {
		setClaim(ALGORITHM, algorithm);
		return this;
	}

	/**
	 * 增加“typ”头信息
	 *
	 * @param type 类型，如JWT
	 * @return this
	 */
	public JWTHeader setType(final String type) {
		setClaim(TYPE, type);
		return this;
	}

	/**
	 * 增加“cty”头信息
	 *
	 * @param contentType 内容类型
	 * @return this
	 */
	public JWTHeader setContentType(final String contentType) {
		setClaim(CONTENT_TYPE, contentType);
		return this;
	}

	/**
	 * 增加“kid”头信息
	 *
	 * @param keyId kid
	 * @return this
	 */
	public JWTHeader setKeyId(final String keyId) {
		setClaim(KEY_ID, keyId);
		return this;
	}

	/**
	 * 增加自定义JWT认证头
	 *
	 * @param headerClaims 头信息
	 * @return this
	 */
	public JWTHeader addHeaders(final Map<String, ?> headerClaims) {
		putAll(headerClaims);
		return this;
	}
}
