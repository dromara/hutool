package org.dromara.hutool.json.jwt.signers;

import org.dromara.hutool.json.jwt.JWTException;

import java.security.Key;
import java.security.KeyPair;

/**
 * 椭圆曲线（Elliptic Curve）的JWT签名器。<br>
 * 按照https://datatracker.ietf.org/doc/html/rfc7518#section-3.4,<br>
 * Elliptic Curve Digital Signature Algorithm (ECDSA)算法签名需要转换DER格式为pair (R, S)
 *
 * @author looly
 * @since 5.8.21
 */
public class EllipticCurveJWTSigner extends AsymmetricJWTSigner {

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 */
	public EllipticCurveJWTSigner(final String algorithm, final Key key) {
		super(algorithm, key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param keyPair   密钥对
	 */
	public EllipticCurveJWTSigner(final String algorithm, final KeyPair keyPair) {
		super(algorithm, keyPair);
	}

	@Override
	protected byte[] sign(final byte[] data) {
		// https://datatracker.ietf.org/doc/html/rfc7518#section-3.4
		return derToConcat(super.sign(data), getSignatureByteArrayLength(getAlgorithm()));
	}

	@Override
	protected boolean verify(final byte[] data, final byte[] signed) {
		// https://datatracker.ietf.org/doc/html/rfc7518#section-3.4
		return super.verify(data, concatToDER(signed));
	}

	/**
	 * 获取签名长度
	 *
	 * @param alg 算法
	 * @return 长度
	 * @throws JWTException JWT异常
	 */
	private static int getSignatureByteArrayLength(final String alg) throws JWTException {
		switch (alg) {
			case "ES256":
			case "SHA256withECDSA":
				return 64;
			case "ES384":
			case "SHA384withECDSA":
				return 96;
			case "ES512":
			case "SHA512withECDSA":
				return 132;
			default:
				throw new JWTException("Unsupported Algorithm: {}", alg);
		}
	}

	/**
	 * DER格式转换为pair (R, S)
	 *
	 * @param derSignature DER格式签名
	 * @param outputLength 算法签名长度
	 * @return pair (R, S)
	 * @throws JWTException JWT异常
	 */
	private static byte[] derToConcat(final byte[] derSignature, final int outputLength) throws JWTException {

		if (derSignature.length < 8 || derSignature[0] != 48) {
			throw new JWTException("Invalid ECDSA signature format");
		}

		final int offset;
		if (derSignature[1] > 0) {
			offset = 2;
		} else if (derSignature[1] == (byte) 0x81) {
			offset = 3;
		} else {
			throw new JWTException("Invalid ECDSA signature format");
		}

		final byte rLength = derSignature[offset + 1];

		int i = rLength;
		while ((i > 0) && (derSignature[(offset + 2 + rLength) - i] == 0)) {
			i--;
		}

		final byte sLength = derSignature[offset + 2 + rLength + 1];

		int j = sLength;
		while ((j > 0) && (derSignature[(offset + 2 + rLength + 2 + sLength) - j] == 0)) {
			j--;
		}

		int rawLen = Math.max(i, j);
		rawLen = Math.max(rawLen, outputLength / 2);

		if ((derSignature[offset - 1] & 0xff) != derSignature.length - offset
			|| (derSignature[offset - 1] & 0xff) != 2 + rLength + 2 + sLength
			|| derSignature[offset] != 2
			|| derSignature[offset + 2 + rLength] != 2) {
			throw new JWTException("Invalid ECDSA signature format");
		}

		final byte[] concatSignature = new byte[2 * rawLen];

		System.arraycopy(derSignature, (offset + 2 + rLength) - i, concatSignature, rawLen - i, i);
		System.arraycopy(derSignature, (offset + 2 + rLength + 2 + sLength) - j, concatSignature, 2 * rawLen - j, j);

		return concatSignature;
	}

	/**
	 * pair (R, S)转换为DER格式
	 *
	 * @param jwsSignature JWT签名
	 * @return DER格式签名
	 */
	private static byte[] concatToDER(final byte[] jwsSignature) {

		final int rawLen = jwsSignature.length / 2;

		int i = rawLen;

		while ((i > 0) && (jwsSignature[rawLen - i] == 0)) {
			i--;
		}

		int j = i;

		if (jwsSignature[rawLen - i] < 0) {
			j += 1;
		}

		int k = rawLen;

		while ((k > 0) && (jwsSignature[2 * rawLen - k] == 0)) {
			k--;
		}

		int l = k;

		if (jwsSignature[2 * rawLen - k] < 0) {
			l += 1;
		}

		final int len = 2 + j + 2 + l;

		if (len > 255) {
			throw new JWTException("Invalid ECDSA signature format");
		}

		int offset;

		final byte[] derSignature;

		if (len < 128) {
			derSignature = new byte[2 + 2 + j + 2 + l];
			offset = 1;
		} else {
			derSignature = new byte[3 + 2 + j + 2 + l];
			derSignature[1] = (byte) 0x81;
			offset = 2;
		}

		derSignature[0] = 48;
		derSignature[offset++] = (byte) len;
		derSignature[offset++] = 2;
		derSignature[offset++] = (byte) j;

		System.arraycopy(jwsSignature, rawLen - i, derSignature, (offset + j) - i, i);

		offset += j;

		derSignature[offset++] = 2;
		derSignature[offset++] = (byte) l;

		System.arraycopy(jwsSignature, 2 * rawLen - k, derSignature, (offset + l) - k, k);

		return derSignature;
	}
}
