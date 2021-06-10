package cn.hutool.jwt.signers;

/**
 * JWT签名接口封装，通过实现此接口，完成不同算法的签名功能
 *
 * @author looly
 */
public interface JWTSigner {

	/**
	 * 签名
	 * @param header JWT头的JSON字符串
	 * @param payload JWT载荷的JSON字符串
	 * @return 签名结果，即JWT的第三部分
	 */
	String sign(String header, String payload);
}
