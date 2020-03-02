package cn.hutool.crypto;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

/**
 * Bouncy Castle相关工具类封装
 *
 * @author looly
 * @since 4.5.0
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
	 * @param encode    压缩公钥
	 * @param curveName EC曲线名
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(String encode, String curveName) {
		return decodeECPoint(SecureUtil.decode(encode), curveName);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param encodeByte 压缩公钥
	 * @param curveName  EC曲线名，例如{@link KeyUtil#SM2_DEFAULT_CURVE}
	 * @return 公钥
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
		ECNamedCurveSpec ecSpec = new ECNamedCurveSpec(curveName, curve, namedSpec.getG(), namedSpec.getN());

		final KeyFactory PubKeyGen = KeyUtil.getKeyFactory("EC");
		try {
			return PubKeyGen.generatePublic(new ECPublicKeySpec(point, ecSpec));
		} catch (GeneralSecurityException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * ECKey转换为ECKeyParameters
	 *
	 * @param ecKey BCECPrivateKey或者BCECPublicKey
	 * @return ECPrivateKeyParameters或者ECPublicKeyParameters
	 * @since 5.1.6
	 */
	public static ECKeyParameters toParams(ECKey ecKey) {
		final ECParameterSpec parameterSpec = ecKey.getParameters();
		final ECDomainParameters ecDomainParameters = buildECDomainParameters(parameterSpec);

		if (ecKey instanceof BCECPrivateKey) {
			return new ECPrivateKeyParameters(((BCECPrivateKey) ecKey).getD(), ecDomainParameters);
		} else if (ecKey instanceof BCECPublicKey) {
			return new ECPublicKeyParameters(((BCECPublicKey) ecKey).getQ(), ecDomainParameters);
		}

		return null;
	}

	/**
	 * 构建ECDomainParameters对象
	 *
	 * @param parameterSpec ECParameterSpec
	 * @return ECDomainParameters
	 * @since 5.1.6
	 */
	public static ECDomainParameters buildECDomainParameters(ECParameterSpec parameterSpec) {
		return new ECDomainParameters(
				parameterSpec.getCurve(),
				parameterSpec.getG(),
				parameterSpec.getN(),
				parameterSpec.getH());
	}
}
