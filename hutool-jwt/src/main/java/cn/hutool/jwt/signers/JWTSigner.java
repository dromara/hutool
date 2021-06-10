package cn.hutool.jwt.signers;

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
	 * @return 签名结果，即JWT的第三部分
	 */
	String sign(String headerBase64, String payloadBase64);

	/**
	 * 验签
	 *
	 * @param headerBase64  JWT头的JSON字符串Base64表示
	 * @param payloadBase64 JWT载荷的JSON字符串Base64表示
	 * @param signBase64 被验证的签名Base64表示
	 * @return 签名是否一致
	 */
	boolean verify(String headerBase64, String payloadBase64, String signBase64);

	/**
	 * 获取算法
	 *
	 * @return 算法
	 */
	String getAlgorithm();
}
