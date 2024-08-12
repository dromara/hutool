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

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.crypto.asymmetric.Sign;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加密JWT签名封装
 *
 * @author looly
 * @since 5.7.0
 */
public class AsymmetricJWTSigner implements JWTSigner {

	private Charset charset = CharsetUtil.UTF_8;
	private final Sign sign;

	/**
	 * 构造
	 *
	 * @param algorithm 算法字符串表示
	 * @param key       公钥{@link PublicKey}或私钥{@link PrivateKey}，公钥用于验证签名，私钥用于产生签名
	 */
	public AsymmetricJWTSigner(final String algorithm, final Key key) {
		final PublicKey publicKey = key instanceof PublicKey ? (PublicKey) key : null;
		final PrivateKey privateKey = key instanceof PrivateKey ? (PrivateKey) key : null;
		this.sign = new Sign(algorithm, privateKey, publicKey);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法字符串表示
	 * @param keyPair   密钥对
	 */
	public AsymmetricJWTSigner(final String algorithm, final KeyPair keyPair) {
		this.sign = new Sign(algorithm, keyPair);
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 编码
	 */
	public AsymmetricJWTSigner setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public String sign(final String headerBase64, final String payloadBase64) {
		final String dataStr = StrUtil.format("{}.{}", headerBase64, payloadBase64);
		return Base64.encodeUrlSafe(sign(ByteUtil.toBytes(dataStr, charset)));
	}

	/**
	 * 签名字符串数据
	 *
	 * @param data 数据
	 * @return 签名
	 */
	protected byte[] sign(final byte[] data) {
		return sign.sign(data);
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		return verify(
				ByteUtil.toBytes(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset),
				Base64.decode(signBase64));
	}

	/**
	 * 验签数据
	 *
	 * @param data   数据
	 * @param signed 签名
	 * @return 是否通过
	 */
	protected boolean verify(final byte[] data, final byte[] signed) {
		return sign.verify(data, signed);
	}

	@Override
	public String getAlgorithm() {
		return this.sign.getSignature().getAlgorithm();
	}

}
