package cn.hutool.crypto;

import cn.hutool.core.util.HexUtil;
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

import java.math.BigInteger;
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
	 * 只获取私钥里的d，32字节
	 *
	 * @param privateKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
	 * @return 压缩得到的X
	 * @since 5.1.6
	 */
	public static byte[] encodeECPrivateKey(PrivateKey privateKey) {
		return ((BCECPrivateKey) privateKey).getD().toByteArray();
	}

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
		final ECDomainParameters ecDomainParameters = toDomainParameters(parameterSpec);

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
	public static ECDomainParameters toDomainParameters(ECParameterSpec parameterSpec) {
		return new ECDomainParameters(
				parameterSpec.getCurve(),
				parameterSpec.getG(),
				parameterSpec.getN(),
				parameterSpec.getH());
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param dHex             私钥d值16进制字符串
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toParams(String dHex, ECDomainParameters domainParameters) {
		return new ECPrivateKeyParameters(new BigInteger(dHex, 16), domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toParams(BigInteger d, ECDomainParameters domainParameters) {
		return new ECPrivateKeyParameters(d, domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param x                公钥X
	 * @param y                公钥Y
	 * @param curve            ECCurve
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(BigInteger x, BigInteger y, ECCurve curve, ECDomainParameters domainParameters) {
		return toParams(x.toByteArray(), y.toByteArray(), curve, domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param xHex             公钥X
	 * @param yHex             公钥Y
	 * @param curve            ECCurve
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(String xHex, String yHex, ECCurve curve, ECDomainParameters domainParameters) {
		return toParams(HexUtil.decodeHex(xHex), HexUtil.decodeHex(yHex),
				curve, domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param xBytes           公钥X
	 * @param yBytes           公钥Y
	 * @param curve            ECCurve
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(byte[] xBytes, byte[] yBytes, ECCurve curve, ECDomainParameters domainParameters) {
		final byte uncompressedFlag = 0x04;
		int curveLength = getCurveLength(domainParameters);
		xBytes = fixLength(curveLength, xBytes);
		yBytes = fixLength(curveLength, yBytes);
		byte[] encodedPubKey = new byte[1 + xBytes.length + yBytes.length];
		encodedPubKey[0] = uncompressedFlag;
		System.arraycopy(xBytes, 0, encodedPubKey, 1, xBytes.length);
		System.arraycopy(yBytes, 0, encodedPubKey, 1 + xBytes.length, yBytes.length);
		return new ECPublicKeyParameters(curve.decodePoint(encodedPubKey), domainParameters);
	}

	/**
	 * 获取Curve长度
	 *
	 * @param ecKey Curve
	 * @return Curve长度
	 */
	public static int getCurveLength(ECKeyParameters ecKey) {
		return getCurveLength(ecKey.getParameters());
	}

	/**
	 * 获取Curve长度
	 *
	 * @param domainParams ECDomainParameters
	 * @return Curve长度
	 */
	public static int getCurveLength(ECDomainParameters domainParams) {
		return (domainParams.getCurve().getFieldSize() + 7) / 8;
	}

	/**
	 * 修正长度
	 *
	 * @param curveLength 修正后的长度
	 * @param src bytes
	 * @return 修正后的bytes
	 */
	private static byte[] fixLength(int curveLength, byte[] src) {
		if (src.length == curveLength) {
			return src;
		}

		byte[] result = new byte[curveLength];
		if (src.length > curveLength) {
			// 裁剪末尾的指定长度
			System.arraycopy(src, src.length - result.length, result, 0, result.length);
		} else {
			// 放置于末尾
			System.arraycopy(src, 0, result, result.length - src.length, src.length);
		}
		return result;
	}
}
