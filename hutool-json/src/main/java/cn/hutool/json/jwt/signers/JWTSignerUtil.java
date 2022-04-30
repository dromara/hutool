package cn.hutool.json.jwt.signers;

import cn.hutool.core.lang.Assert;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * JWT签名器工具类
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTSignerUtil {

	/**
	 * 无签名
	 *
	 * @return 无签名的签名器
	 */
	public static JWTSigner none() {
		return NoneJWTSigner.NONE;
	}

	//------------------------------------------------------------------------- HSxxx

	/**
	 * HS256(HmacSHA256)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner hs256(final byte[] key) {
		return createSigner("HS256", key);
	}

	/**
	 * HS384(HmacSHA384)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner hs384(final byte[] key) {
		return createSigner("HS384", key);
	}

	/**
	 * HS512(HmacSHA512)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner hs512(final byte[] key) {
		return createSigner("HS512", key);
	}

	//------------------------------------------------------------------------- RSxxx

	/**
	 * RS256(SHA256withRSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner rs256(final Key key) {
		return createSigner("RS256", key);
	}

	/**
	 * RS384(SHA384withRSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner rs384(final Key key) {
		return createSigner("RS384", key);
	}

	/**
	 * RS512(SHA512withRSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner rs512(final Key key) {
		return createSigner("RS512", key);
	}

	//------------------------------------------------------------------------- ESxxx

	/**
	 * ES256(SHA256withECDSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner es256(final Key key) {
		return createSigner("ES256", key);
	}

	/**
	 * ES384(SHA383withECDSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner es384(final Key key) {
		return createSigner("ES384", key);
	}

	/**
	 * ES512(SHA512withECDSA)签名器
	 *
	 * @param key 密钥
	 * @return 签名器
	 */
	public static JWTSigner es512(final Key key) {
		return createSigner("ES512", key);
	}

	/**
	 * 创建签名器
	 *
	 * @param algorithmId 算法ID，见{@link AlgorithmUtil}
	 * @param key         密钥
	 * @return 签名器
	 */
	public static JWTSigner createSigner(final String algorithmId, final byte[] key) {
		Assert.notNull(key, "Signer key must be not null!");

		if (null == algorithmId || NoneJWTSigner.ID_NONE.equals(algorithmId)) {
			return none();
		}
		return new HMacJWTSigner(AlgorithmUtil.getAlgorithm(algorithmId), key);
	}

	/**
	 * 创建签名器
	 *
	 * @param algorithmId 算法ID，见{@link AlgorithmUtil}
	 * @param keyPair     密钥对
	 * @return 签名器
	 */
	public static JWTSigner createSigner(final String algorithmId, final KeyPair keyPair) {
		Assert.notNull(keyPair, "Signer key pair must be not null!");

		if (null == algorithmId || NoneJWTSigner.ID_NONE.equals(algorithmId)) {
			return none();
		}
		return new AsymmetricJWTSigner(AlgorithmUtil.getAlgorithm(algorithmId), keyPair);
	}

	/**
	 * 创建签名器
	 *
	 * @param algorithmId 算法ID，见{@link AlgorithmUtil}
	 * @param key         密钥
	 * @return 签名器
	 */
	public static JWTSigner createSigner(final String algorithmId, final Key key) {
		Assert.notNull(key, "Signer key must be not null!");

		if (null == algorithmId || NoneJWTSigner.ID_NONE.equals(algorithmId)) {
			return NoneJWTSigner.NONE;
		}
		if (key instanceof PrivateKey || key instanceof PublicKey) {
			return new AsymmetricJWTSigner(AlgorithmUtil.getAlgorithm(algorithmId), key);
		}
		return new HMacJWTSigner(AlgorithmUtil.getAlgorithm(algorithmId), key);
	}
}
