package cn.hutool.jwt;

import java.util.Date;
import java.util.Map;

/**
 * JWT载荷信息<br>
 * 载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分:
 *
 * <ul>
 *     <li>标准中注册的声明</li>
 *     <li>公共的声明</li>
 *     <li>私有的声明</li>
 * </ul>
 * <p>
 * 详细介绍见：https://www.jianshu.com/p/576dbf44b2ae
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTPayload extends Claims {
	private static final long serialVersionUID = 1L;

	/**
	 * jwt签发者
	 */
	public static String ISSUER = "iss";
	/**
	 * jwt所面向的用户
	 */
	public static String SUBJECT = "sub";
	/**
	 * 接收jwt的一方
	 */
	public static String AUDIENCE = "aud";
	/**
	 * jwt的过期时间，这个过期时间必须要大于签发时间
	 */
	public static String EXPIRES_AT = "exp";
	/**
	 * 定义在什么时间之前，该jwt都是不可用的.
	 */
	public static String NOT_BEFORE = "nbf";
	/**
	 * jwt的签发时间
	 */
	public static String ISSUED_AT = "iat";
	/**
	 * jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
	 */
	public static String JWT_ID = "jti";

	/**
	 * 设置 jwt签发者("iss")的Payload值
	 *
	 * @param issuer jwt签发者
	 * @return this
	 */
	public JWTPayload setIssuer(String issuer) {
		setClaim(ISSUER, issuer);
		return this;
	}

	/**
	 * 设置jwt所面向的用户("sub")的Payload值
	 *
	 * @param subject jwt所面向的用户
	 * @return this
	 */
	public JWTPayload setSubject(String subject) {
		setClaim(SUBJECT, subject);
		return this;
	}

	/**
	 * 设置接收jwt的一方("aud")的Payload值
	 *
	 * @param audience 接收jwt的一方
	 * @return this
	 */
	public JWTPayload setAudience(String... audience) {
		setClaim(AUDIENCE, audience);
		return this;
	}

	/**
	 * Add a specific Expires At ("exp") claim to the Payload.
	 * 设置jwt的过期时间("exp")的Payload值，这个过期时间必须要大于签发时间
	 *
	 * @param expiresAt jwt的过期时间
	 * @return this
	 * @see #setIssuedAt(Date)
	 */
	public JWTPayload setExpiresAt(Date expiresAt) {
		setClaim(EXPIRES_AT, expiresAt);
		return this;
	}

	/**
	 * 设置不可用时间点界限("nbf")的Payload值
	 *
	 * @param notBefore 不可用时间点界限，在这个时间点之前，jwt不可用
	 * @return this
	 */
	public JWTPayload setNotBefore(Date notBefore) {
		setClaim(NOT_BEFORE, notBefore);
		return this;
	}

	/**
	 * 设置jwt的签发时间("iat")
	 *
	 * @param issuedAt 签发时间
	 * @return this
	 */
	public JWTPayload setIssuedAt(Date issuedAt) {
		setClaim(ISSUED_AT, issuedAt);
		return this;
	}

	/**
	 * 设置jwt的唯一身份标识("jti")
	 *
	 * @param jwtId 唯一身份标识
	 * @return this
	 */
	public JWTPayload setJWTId(String jwtId) {
		setClaim(JWT_ID, jwtId);
		return this;
	}

	/**
	 * 增加自定义JWT认证载荷信息
	 *
	 * @param payloadClaims 载荷信息
	 * @return this
	 */
	public JWTPayload addPayloads(Map<String, ?> payloadClaims) {
		putAll(payloadClaims);
		return this;
	}
}
