package cn.hutool.jwt;

import cn.hutool.jwt.signers.JWTSigner;

import java.util.Map;

/**
 * JSON Web Token (JWT)工具类
 */
public class JWTUtil {

	/**
	 * 创建HS256(HmacSHA256) JWT Token
	 *
	 * @param payload 荷载信息
	 * @param key     HS256(HmacSHA256)密钥
	 * @return JWT Token
	 */
	public static String createToken(Map<String, Object> payload, byte[] key) {
		return createToken(null, payload, key);
	}

	/**
	 * 创建HS256(HmacSHA256) JWT Token
	 *
	 * @param headers 头信息
	 * @param payload 荷载信息
	 * @param key     HS256(HmacSHA256)密钥
	 * @return JWT Token
	 */
	public static String createToken(Map<String, Object> headers, Map<String, Object> payload, byte[] key) {
		return JWT.create()
				.addHeaders(headers)
				.addPayloads(payload)
				.setKey(key)
				.sign();
	}

	/**
	 * 创建JWT Token
	 *
	 * @param payload 荷载信息
	 * @param signer  签名算法
	 * @return JWT Token
	 */
	public static String createToken(Map<String, Object> payload, JWTSigner signer) {
		return createToken(null, payload, signer);
	}

	/**
	 * 创建JWT Token
	 *
	 * @param headers 头信息
	 * @param payload 荷载信息
	 * @param signer  签名算法
	 * @return JWT Token
	 */
	public static String createToken(Map<String, Object> headers, Map<String, Object> payload, JWTSigner signer) {
		return JWT.create()
				.addHeaders(headers)
				.addPayloads(payload)
				.setSigner(signer)
				.sign();
	}

	/**
	 * 解析JWT Token
	 *
	 * @param token token
	 * @return {@link JWT}
	 */
	public JWT parseToken(String token) {
		return JWT.of(token);
	}

	/**
	 * 验证JWT Token有效性
	 *
	 * @param token JWT Token
	 * @param key   HS256(HmacSHA256)密钥
	 * @return 是否有效
	 */
	public boolean verify(String token, byte[] key) {
		return JWT.of(token).setKey(key).verify();
	}

	/**
	 * 验证JWT Token有效性
	 *
	 * @param token  JWT Token
	 * @param signer 签名器
	 * @return 是否有效
	 */
	public boolean verify(String token, JWTSigner signer) {
		return JWT.of(token).verify(signer);
	}
}
