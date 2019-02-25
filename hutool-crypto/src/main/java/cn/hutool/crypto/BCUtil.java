package cn.hutool.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;

/**
 * Bouncy Castle相关工具类封装
 * 
 * @author looly
 *@since 4.5.0
 */
public class BCUtil {
	/**
	 * 编码压缩EC公钥（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 * 
	 * @param publicKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
	 * @return 压缩得到的X
	 * @since 4.4.4
	 */
	public static byte[] encodeECPublicKey(PublicKey publicKey) {
		return ((BCECPublicKey) publicKey).getQ().getEncoded(true);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 * 
	 * @param encode 压缩公钥
	 * @param curveName EC曲线名
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(String encode, String curveName) {
		return decodeECPoint(SecureUtil.decodeKey(encode), curveName);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 * 
	 * @param encodeByte 压缩公钥
	 * @param curveName EC曲线名，例如{@link KeyUtil#SM2_DEFAULT_CURVE}
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(byte[] encodeByte, String curveName) {
		final ECNamedCurveParameterSpec namedSpec = ECNamedCurveTable.getParameterSpec(curveName);
		final ECCurve curve = namedSpec.getCurve();
		final EllipticCurve ecCurve = new EllipticCurve(//
				new ECFieldFp(curve.getField().getCharacteristic()), //
				curve.getA().toBigInteger(), //
				curve.getB().toBigInteger());
		// 根据X恢复点Y
		final ECPoint point = ECPointUtil.decodePoint(ecCurve, encodeByte);

		// 根据曲线恢复公钥格式
		ECParameterSpec ecSpec = new ECNamedCurveSpec(curveName, curve, namedSpec.getG(), namedSpec.getN());

		final KeyFactory PubKeyGen = KeyUtil.getKeyFactory("EC");
		try {
			return PubKeyGen.generatePublic(new ECPublicKeySpec(point, ecSpec));
		} catch (GeneralSecurityException e) {
			throw new CryptoException(e);
		}
	}
}
