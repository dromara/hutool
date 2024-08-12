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

package org.dromara.hutool.json.jwt.signers;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.crypto.digest.mac.HMac;

import java.nio.charset.Charset;
import java.security.Key;

/**
 * HMac算法签名实现
 *
 * @author looly
 * @since 5.7.0
 */
public class HMacJWTSigner implements JWTSigner {

	private Charset charset = CharsetUtil.UTF_8;
	private final HMac hMac;

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(final String algorithm, final byte[] key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(final String algorithm, final Key key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 编码
	 */
	public HMacJWTSigner setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public String sign(final String headerBase64, final String payloadBase64) {
		return hMac.digestBase64(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset, true);
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		final String sign = sign(headerBase64, payloadBase64);
		return hMac.verify(
				ByteUtil.toBytes(sign, charset),
				ByteUtil.toBytes(signBase64, charset));
	}

	@Override
	public String getAlgorithm() {
		return this.hMac.getAlgorithm();
	}
}
