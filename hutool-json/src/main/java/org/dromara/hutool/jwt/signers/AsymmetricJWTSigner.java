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

package org.dromara.hutool.jwt.signers;

import org.dromara.hutool.codec.binary.Base64;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.ByteUtil;
import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.asymmetric.Sign;

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
		return Base64.encodeUrlSafe(sign.sign(StrUtil.format("{}.{}", headerBase64, payloadBase64)));
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		return sign.verify(
				ByteUtil.toBytes(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset),
				Base64.decode(signBase64));
	}

	@Override
	public String getAlgorithm() {
		return this.sign.getSignature().getAlgorithm();
	}

}
