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

package org.dromara.hutool.jwt;

import org.dromara.hutool.JSONObject;
import org.dromara.hutool.codec.binary.Base64;
import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.exceptions.ValidateException;
import org.dromara.hutool.jwt.signers.AlgorithmUtil;
import org.dromara.hutool.jwt.signers.JWTSigner;
import org.dromara.hutool.jwt.signers.JWTSignerUtil;
import org.dromara.hutool.jwt.signers.NoneJWTSigner;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.text.split.SplitUtil;
import org.dromara.hutool.util.CharsetUtil;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.util.List;
import java.util.Map;

/**
 * JSON Web Token (JWT)，基于JSON的开放标准（(RFC 7519)用于在网络应用环境间传递声明。<br>
 * <p>
 * 结构：header.payload.signature
 * <ul>
 *     <li>header：主要声明了JWT的签名算法</li>
 *     <li>payload：主要承载了各种声明并传递明文数据</li>
 *     <li>signature：拥有该部分的JWT被称为JWS，也就是签了名的JWS</li>
 * </ul>
 *
 * <p>
 * 详细介绍见；<a href="https://www.jianshu.com/p/576dbf44b2ae">https://www.jianshu.com/p/576dbf44b2ae</a>
 * </p>
 *
 * @author looly
 * @since 5.7.0
 */
public class JWT implements RegisteredPayload<JWT> {

	private final JWTHeader header;
	private final JWTPayload payload;

	private Charset charset;
	private JWTSigner signer;

	private List<String> tokens;

	/**
	 * 创建空的JWT对象
	 *
	 * @return JWT
	 */
	public static JWT of() {
		return new JWT();
	}

	/**
	 * 创建并解析JWT对象
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz
	 * @return JWT
	 */
	public static JWT of(final String token) {
		return new JWT(token);
	}

	/**
	 * 构造
	 */
	public JWT() {
		this.header = new JWTHeader();
		this.payload = new JWTPayload();
		this.charset = CharsetUtil.UTF_8;
	}

	/**
	 * 构造
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz
	 */
	public JWT(final String token) {
		this();
		parse(token);
	}

	/**
	 * 解析JWT内容
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz，不能为空
	 * @return this
	 * @throws IllegalArgumentException 给定字符串为空
	 */
	public JWT parse(final String token) throws IllegalArgumentException {
		Assert.notBlank(token, "Token String must be not blank!");
		final List<String> tokens = splitToken(token);
		this.tokens = tokens;
		this.header.parse(tokens.get(0), this.charset);
		this.payload.parse(tokens.get(1), this.charset);
		return this;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public JWT setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置密钥，默认算法是：HS256(HmacSHA256)
	 *
	 * @param key 密钥
	 * @return this
	 */
	public JWT setKey(final byte[] key) {
		// 检查头信息中是否有算法信息
		final String algorithmId = (String) this.header.getClaim(JWTHeader.ALGORITHM);
		if (StrUtil.isNotBlank(algorithmId)) {
			return setSigner(algorithmId, key);
		}
		return setSigner(JWTSignerUtil.hs256(key));
	}

	/**
	 * 设置签名算法
	 *
	 * @param algorithmId 签名算法ID，如HS256
	 * @param key         密钥
	 * @return this
	 */
	public JWT setSigner(final String algorithmId, final byte[] key) {
		return setSigner(JWTSignerUtil.createSigner(algorithmId, key));
	}

	/**
	 * 设置签名算法
	 *
	 * @param algorithmId 签名算法ID，如HS256
	 * @param key         密钥
	 * @return this
	 * @since 5.7.2
	 */
	public JWT setSigner(final String algorithmId, final Key key) {
		return setSigner(JWTSignerUtil.createSigner(algorithmId, key));
	}

	/**
	 * 设置非对称签名算法
	 *
	 * @param algorithmId 签名算法ID，如HS256
	 * @param keyPair     密钥对
	 * @return this
	 * @since 5.7.2
	 */
	public JWT setSigner(final String algorithmId, final KeyPair keyPair) {
		return setSigner(JWTSignerUtil.createSigner(algorithmId, keyPair));
	}

	/**
	 * 设置签名算法
	 *
	 * @param signer 签名算法
	 * @return this
	 */
	public JWT setSigner(final JWTSigner signer) {
		this.signer = signer;
		return this;
	}

	/**
	 * 获取JWT算法签名器
	 *
	 * @return JWT算法签名器
	 */
	public JWTSigner getSigner() {
		return this.signer;
	}

	/**
	 * 获取所有头信息
	 *
	 * @return 头信息
	 */
	public JSONObject getHeaders() {
		return this.header.getClaimsJson();
	}

	/**
	 * 获取头
	 *
	 * @return 头信息
	 * @since 5.7.2
	 */
	public JWTHeader getHeader() {
		return this.header;
	}

	/**
	 * 获取头信息
	 *
	 * @param name 头信息名称
	 * @return 头信息
	 */
	public Object getHeader(final String name) {
		return this.header.getClaim(name);
	}

	/**
	 * 获取算法ID(alg)头信息
	 *
	 * @return 算法头信息
	 * @see JWTHeader#ALGORITHM
	 */
	public String getAlgorithm() {
		return (String) this.header.getClaim(JWTHeader.ALGORITHM);
	}

	/**
	 * 设置JWT头信息
	 *
	 * @param name  头名
	 * @param value 头
	 * @return this
	 */
	public JWT setHeader(final String name, final Object value) {
		this.header.setClaim(name, value);
		return this;
	}

	/**
	 * 增加JWT头信息
	 *
	 * @param headers 头信息
	 * @return this
	 */
	public JWT addHeaders(final Map<String, ?> headers) {
		this.header.addHeaders(headers);
		return this;
	}

	/**
	 * 获取所有载荷信息
	 *
	 * @return 载荷信息
	 */
	public JSONObject getPayloads() {
		return this.payload.getClaimsJson();
	}

	/**
	 * 获取载荷对象
	 *
	 * @return 载荷信息
	 * @since 5.7.2
	 */
	public JWTPayload getPayload() {
		return this.payload;
	}

	/**
	 * 获取载荷信息
	 *
	 * @param name 载荷信息名称
	 * @return 载荷信息
	 */
	public Object getPayload(final String name) {
		return getPayload().getClaim(name);
	}

	/**
	 * 获取payload并获取类型
	 *
	 * @param <T>          Bean类型
	 * @param propertyName 需要提取的属性名称
	 * @param propertyType 需要提取的属性类型
	 * @return 载荷信息
	 * @throws ValidateException 传入的类型不匹配payload类型
	 * @since 6.0.0
	 */
	public <T> T getPayload(final String propertyName, final Class<T> propertyType) {
		return getPayload().getClaimsJson().get(propertyName, propertyType);
	}

	/**
	 * 设置JWT载荷信息
	 *
	 * @param name  载荷名
	 * @param value 头
	 * @return this
	 */
	@Override
	public JWT setPayload(final String name, final Object value) {
		this.payload.setClaim(name, value);
		return this;
	}

	/**
	 * 增加JWT载荷信息
	 *
	 * @param payloads 载荷信息
	 * @return this
	 */
	public JWT addPayloads(final Map<String, ?> payloads) {
		this.payload.addPayloads(payloads);
		return this;
	}

	/**
	 * 签名生成JWT字符串
	 *
	 * @return JWT字符串
	 */
	public String sign() {
		return sign(this.signer);
	}

	/**
	 * 签名生成JWT字符串，计算方式为（以HS256为例）：
	 * <pre>
	 * HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
	 * </pre>
	 *
	 * <p>此方法会补充如下的header：</p>
	 * <ul>
	 *     <li>当用户未定义"typ"时，赋默认值："JWT"</li>
	 *     <li>当用户未定义"alg"时，根据传入的{@link JWTSigner}对象类型，赋值对应ID</li>
	 * </ul>
	 *
	 * @param signer 自定义JWT签名器，非空
	 * @return JWT字符串
	 */
	public String sign(final JWTSigner signer) {
		Assert.notNull(signer, () -> new JWTException("No Signer provided!"));

		// 检查tye信息
		final String type = (String) this.header.getClaim(JWTHeader.TYPE);
		if (StrUtil.isBlank(type)) {
			this.header.setType("JWT");
		}

		// 检查头信息中是否有算法信息
		final String algorithm = (String) this.header.getClaim(JWTHeader.ALGORITHM);
		if (StrUtil.isBlank(algorithm)) {
			this.header.setAlgorithm(AlgorithmUtil.getId(signer.getAlgorithm()));
		}

		final String headerBase64 = Base64.encodeUrlSafe(this.header.toString(), charset);
		final String payloadBase64 = Base64.encodeUrlSafe(this.payload.toString(), charset);
		final String sign = signer.sign(headerBase64, payloadBase64);

		return StrUtil.format("{}.{}.{}", headerBase64, payloadBase64, sign);
	}

	/**
	 * 验证JWT Token是否有效
	 *
	 * @return 是否有效
	 */
	public boolean verify() {
		return verify(this.signer);
	}

	/**
	 * 验证JWT是否有效，验证包括：
	 *
	 * <ul>
	 *     <li>Token是否正确</li>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：生效时间不能晚于当前时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：失效时间不能早于当前时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}： 签发时间不能晚于当前时间</li>
	 * </ul>
	 *
	 * @param leeway 容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @return 是否有效
	 * @see JWTValidator
	 * @since 5.7.4
	 */
	public boolean validate(final long leeway) {
		if (false == verify()) {
			return false;
		}

		// 校验时间字段
		try {
			JWTValidator.of(this).validateDate(DateUtil.now(), leeway);
		} catch (final ValidateException e) {
			return false;
		}

		return true;
	}

	/**
	 * 使用指定签名器，验证JWT Token是否有效<br>
	 * 如果签名器为{@code null}，或者{@link NoneJWTSigner}，表示这个JWT无签名，签名部分必须为空
	 *
	 * @param signer 签名器（签名算法），如果为{@code null}，默认为{@link NoneJWTSigner}
	 * @return 是否有效
	 */
	public boolean verify(JWTSigner signer) {
		if (null == signer) {
			// 如果无签名器提供，默认认为是无签名JWT信息
			signer = NoneJWTSigner.NONE;
		}

		final List<String> tokens = this.tokens;
		if (CollUtil.isEmpty(tokens)) {
			throw new JWTException("No token to verify!");
		}
		return signer.verify(tokens.get(0), tokens.get(1), tokens.get(2));
	}

	/**
	 * 将JWT字符串拆分为3部分，无加密算法则最后一部分是""
	 *
	 * @param token JWT Token
	 * @return 三部分内容
	 */
	private static List<String> splitToken(final String token) {
		final List<String> tokens = SplitUtil.split(token, StrUtil.DOT);
		if (3 != tokens.size()) {
			throw new JWTException("The token was expected 3 parts, but got {}.", tokens.size());
		}
		return tokens;
	}
}
