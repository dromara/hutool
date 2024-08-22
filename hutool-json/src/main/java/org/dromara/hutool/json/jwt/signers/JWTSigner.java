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

package org.dromara.hutool.json.jwt.signers;

/**
 * JWT签名接口封装，通过实现此接口，完成不同算法的签名功能
 *
 * @author looly
 */
public interface JWTSigner {

	/**
	 * 签名
	 *
	 * @param headerBase64  JWT头的JSON字符串的Base64表示
	 * @param payloadBase64 JWT载荷的JSON字符串Base64表示
	 * @return 签名结果Base64，即JWT的第三部分
	 */
	String sign(String headerBase64, String payloadBase64);

	/**
	 * 验签
	 *
	 * @param headerBase64  JWT头的JSON字符串Base64表示
	 * @param payloadBase64 JWT载荷的JSON字符串Base64表示
	 * @param signBase64    被验证的签名Base64表示
	 * @return 签名是否一致
	 */
	boolean verify(String headerBase64, String payloadBase64, String signBase64);

	/**
	 * 获取算法
	 *
	 * @return 算法
	 */
	String getAlgorithm();

	/**
	 * 获取算法ID，即算法的简写形式，如HS256
	 *
	 * @return 算法ID
	 * @since 5.7.2
	 */
	default String getAlgorithmId() {
		return AlgorithmUtil.getId(getAlgorithm());
	}
}
