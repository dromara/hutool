package cn.hutool.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * JSON Web Token (JWT)，基于JSON的开放标准（(RFC 7519)用于在网络应用环境间传递声明。<br>
 * <p>
 * 结构：xxxxx.yyyyy.zzzzz
 * <ul>
 *     <li>header：主要声明了JWT的签名算法</li>
 *     <li>payload：主要承载了各种声明并传递明文数据</li>
 *     <li>signture：拥有该部分的JWT被称为JWS，也就是签了名的JWS</li>
 * </ul>
 *
 * <p>
 * 详细介绍见；https://www.jianshu.com/p/576dbf44b2ae
 * </p>
 *
 * @author looly
 */
public class JWT {

	private final JWTHeader header;
	private final JWTPayload payload;

	private Charset charset;
	private JWTSigner signer;

	private List<String> tokens;

	/**
	 * 创建空的JWT对象
	 *
	 * @return {@link JWT}
	 */
	public static JWT create() {
		return new JWT();
	}

	/**
	 * 创建并解析JWT对象
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz
	 * @return {@link JWT}
	 */
	public static JWT of(String token) {
		return new JWT(token);
	}

	/**
	 * 构造
	 */
	public JWT() {
		this.header = new JWTHeader();
		this.payload = new JWTPayload();
		this.charset = CharsetUtil.CHARSET_UTF_8;
	}

	/**
	 * 构造
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz
	 */
	public JWT(String token) {
		this();
		parse(token);
	}

	/**
	 * 解析JWT内容
	 *
	 * @param token JWT Token字符串，格式为xxxx.yyyy.zzzz
	 * @return this
	 */
	public JWT parse(String token) {
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
	public JWT setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置密钥，默认算法是：HS256(HmacSHA256)
	 *
	 * @param key 密钥
	 * @return this
	 */
	public JWT setKey(byte[] key) {
		return setSigner(JWTSignerUtil.hs256(key));
	}

	/**
	 * 设置签名算法
	 *
	 * @param algorithmId 签名算法ID，如HS256
	 * @param key         密钥
	 * @return this
	 */
	public JWT setSigner(String algorithmId, byte[] key) {
		return setSigner(JWTSignerUtil.createSigner(algorithmId, key));
	}

	/**
	 * 设置签名算法
	 *
	 * @param signer 签名算法
	 * @return this
	 */
	public JWT setSigner(JWTSigner signer) {
		this.signer = signer;
		return this;
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
	 * 获取头信息
	 *
	 * @param name 头信息名称
	 * @return 头信息
	 */
	public Object getHeader(String name) {
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
	public JWT setHeader(String name, Object value) {
		this.header.setClaim(name, value);
		return this;
	}

	/**
	 * 增加JWT头信息
	 *
	 * @param headers 头信息
	 * @return this
	 */
	public JWT addHeaders(Map<String, ?> headers) {
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
	 * 获取载荷信息
	 *
	 * @param name 载荷信息名称
	 * @return 载荷信息
	 */
	public Object getPayload(String name) {
		return this.payload.getClaim(name);
	}

	/**
	 * 设置JWT载荷信息
	 *
	 * @param name  载荷名
	 * @param value 头
	 * @return this
	 */
	public JWT setPayload(String name, Object value) {
		this.payload.setClaim(name, value);
		return this;
	}

	/**
	 * 增加JWT载荷信息
	 *
	 * @param payloads 载荷信息
	 * @return this
	 */
	public JWT addPayloads(Map<String, ?> payloads) {
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
	 * 签名生成JWT字符串
	 *
	 * @param signer JWT签名器
	 * @return JWT字符串
	 */
	public String sign(JWTSigner signer) {
		Assert.notNull(signer, () -> new JWTException("No Signer provided!"));

		// 检查头信息中是否有算法信息
		final String claim = (String) this.header.getClaim(JWTHeader.ALGORITHM);
		if (StrUtil.isBlank(claim)) {
			this.header.setClaim(JWTHeader.ALGORITHM,
					AlgorithmUtil.getId(signer.getAlgorithm()));
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
	 * 验证JWT Token是否有效
	 *
	 * @param signer 签名器（签名算法）
	 * @return 是否有效
	 */
	public boolean verify(JWTSigner signer) {
		Assert.notNull(signer, () -> new JWTException("No Signer provided!"));

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
	private static List<String> splitToken(String token) {
		final List<String> tokens = StrUtil.split(token, CharUtil.DOT);
		if (3 != tokens.size()) {
			throw new JWTException("The token was expected 3 parts, but got {}.", tokens.size());
		}
		return tokens;
	}
}
