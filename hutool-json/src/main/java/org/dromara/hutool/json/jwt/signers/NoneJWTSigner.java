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

import org.dromara.hutool.core.text.StrUtil;

/**
 * 无需签名的JWT签名器
 *
 * @author looly
 * @since 5.7.0
 */
public class NoneJWTSigner implements JWTSigner {

	public static final String ID_NONE = "none";

	public static NoneJWTSigner NONE = new NoneJWTSigner();

	@Override
	public String sign(final String headerBase64, final String payloadBase64) {
		return StrUtil.EMPTY;
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		return StrUtil.isEmpty(signBase64);
	}

	@Override
	public String getAlgorithm() {
		return ID_NONE;
	}
}
