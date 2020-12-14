package cn.hutool.crypto;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;

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
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）
	 *
	 * @param encodeByte 压缩公钥
	 * @param curveName  EC曲线名，例如{@link SmUtil#SM2_DOMAIN_PARAMS}
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(byte[] encodeByte, String curveName) {
		final X9ECParameters x9ECParameters = ECUtil.getNamedCurveByName(curveName);
		final ECCurve curve = x9ECParameters.getCurve();
		final ECPoint point = EC5Util.convertPoint(curve.decodePoint(encodeByte));

		// 根据曲线恢复公钥格式
		final ECNamedCurveSpec ecSpec = new ECNamedCurveSpec(curveName, curve, x9ECParameters.getG(), x9ECParameters.getN());
		return KeyUtil.generatePublicKey("EC", new ECPublicKeySpec(point, ecSpec));
	}

	/**
	 * 构建ECDomainParameters对象
	 *
	 * @param parameterSpec ECParameterSpec
	 * @return {@link ECDomainParameters}
	 * @since 5.2.0
	 */
	public static ECDomainParameters toDomainParams(ECParameterSpec parameterSpec) {
		return new ECDomainParameters(
				parameterSpec.getCurve(),
				parameterSpec.getG(),
				parameterSpec.getN(),
				parameterSpec.getH());
	}

	/**
	 * 构建ECDomainParameters对象
	 *
	 * @param curveName Curve名称
	 * @return {@link ECDomainParameters}
	 * @since 5.2.0
	 */
	public static ECDomainParameters toDomainParams(String curveName) {
		return toDomainParams(ECUtil.getNamedCurveByName(curveName));
	}

	/**
	 * 构建ECDomainParameters对象
	 *
	 * @param x9ECParameters {@link X9ECParameters}
	 * @return {@link ECDomainParameters}
	 * @since 5.2.0
	 */
	public static ECDomainParameters toDomainParams(X9ECParameters x9ECParameters) {
		return new ECDomainParameters(
				x9ECParameters.getCurve(),
				x9ECParameters.getG(),
				x9ECParameters.getN(),
				x9ECParameters.getH()
		);
	}

	/**
	 * 密钥转换为AsymmetricKeyParameter
	 *
	 * @param key PrivateKey或者PublicKey
	 * @return ECPrivateKeyParameters或者ECPublicKeyParameters
	 * @since 5.2.0
	 */
	public static AsymmetricKeyParameter toParams(Key key) {
		return ECKeyUtil.toParams(key);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2Params(String d) {
		return ECKeyUtil.toSm2PrivateParams(d);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param dHex             私钥d值16进制字符串
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toParams(String dHex, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPrivateParams(dHex, domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2Params(byte[] d) {
		return ECKeyUtil.toSm2PrivateParams(d);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toParams(byte[] d, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPrivateParams(d, domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2Params(BigInteger d) {
		return ECKeyUtil.toSm2PrivateParams(d);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toParams(BigInteger d, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPrivateParams(d, domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param x                公钥X
	 * @param y                公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(BigInteger x, BigInteger y, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPublicParams(x, y, domainParameters);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param xHex 公钥X
	 * @param yHex 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2Params(String xHex, String yHex) {
		return ECKeyUtil.toSm2PublicParams(xHex, yHex);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param xHex             公钥X
	 * @param yHex             公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(String xHex, String yHex, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPublicParams(xHex, yHex, domainParameters);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param xBytes 公钥X
	 * @param yBytes 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2Params(byte[] xBytes, byte[] yBytes) {
		return ECKeyUtil.toSm2PublicParams(xBytes, yBytes);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param xBytes           公钥X
	 * @param yBytes           公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toParams(byte[] xBytes, byte[] yBytes, ECDomainParameters domainParameters) {
		return ECKeyUtil.toPublicParams(xBytes, yBytes, domainParameters);
	}

	/**
	 * 公钥转换为 {@link ECPublicKeyParameters}
	 *
	 * @param publicKey 公钥，传入null返回null
	 * @return {@link ECPublicKeyParameters}或null
	 */
	public static ECPublicKeyParameters toParams(PublicKey publicKey) {
		return ECKeyUtil.toPublicParams(publicKey);
	}

	/**
	 * 私钥转换为 {@link ECPrivateKeyParameters}
	 *
	 * @param privateKey 私钥，传入null返回null
	 * @return {@link ECPrivateKeyParameters}或null
	 */
	public static ECPrivateKeyParameters toParams(PrivateKey privateKey) {
		return ECKeyUtil.toPrivateParams(privateKey);
	}

	/**
	 * 读取PEM格式的私钥
	 *
	 * @param pemStream pem流
	 * @return {@link PrivateKey}
	 * @since 5.2.5
	 * @see PemUtil#readPemPrivateKey(InputStream)
	 */
	public static PrivateKey readPemPrivateKey(InputStream pemStream) {
		return PemUtil.readPemPrivateKey(pemStream);
	}

	/**
	 * 读取PEM格式的公钥
	 *
	 * @param pemStream pem流
	 * @return {@link PublicKey}
	 * @since 5.2.5
	 * @see PemUtil#readPemPublicKey(InputStream)
	 */
	public static PublicKey readPemPublicKey(InputStream pemStream) {
		return PemUtil.readPemPublicKey(pemStream);
	}
}
