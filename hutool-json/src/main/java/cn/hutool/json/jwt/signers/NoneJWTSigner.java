package cn.hutool.json.jwt.signers;

import cn.hutool.core.text.StrUtil;

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
