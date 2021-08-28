package cn.hutool.crypto;

import cn.hutool.core.io.IORuntimeException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.spec.OpenSSHPrivateKeySpec;
import org.bouncycastle.jcajce.spec.OpenSSHPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;

/**
 * EC密钥参数相关工具类封装
 *
 * @author looly
 * @since 5.4.3
 */
public class ECKeyUtil {

	/**
	 * 密钥转换为AsymmetricKeyParameter
	 *
	 * @param key PrivateKey或者PublicKey
	 * @return ECPrivateKeyParameters或者ECPublicKeyParameters
	 */
	public static AsymmetricKeyParameter toParams(Key key) {
		if (key instanceof PrivateKey) {
			return toPrivateParams((PrivateKey) key);
		} else if (key instanceof PublicKey) {
			return toPublicParams((PublicKey) key);
		}

		return null;
	}

	/**
	 * 根据私钥参数获取公钥参数
	 *
	 * @param privateKeyParameters 私钥参数
	 * @return 公钥参数
	 * @since 5.5.9
	 */
	public static ECPublicKeyParameters getPublicParams(ECPrivateKeyParameters privateKeyParameters) {
		final ECDomainParameters domainParameters = privateKeyParameters.getParameters();
		final ECPoint q = new FixedPointCombMultiplier().multiply(domainParameters.getG(), privateKeyParameters.getD());
		return new ECPublicKeyParameters(q, domainParameters);
	}

	//--------------------------------------------------------------------------- Public Key

	/**
	 * 转换为 ECPublicKeyParameters
	 *
	 * @param q 公钥Q值
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(byte[] q) {
		return toPublicParams(q, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPublicKeyParameters
	 *
	 * @param q 公钥Q值
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(String q) {
		return toPublicParams(q, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param x 公钥X
	 * @param y 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(String x, String y) {
		return toPublicParams(x, y, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param xBytes 公钥X
	 * @param yBytes 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(byte[] xBytes, byte[] yBytes) {
		return toPublicParams(xBytes, yBytes, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param x                公钥X
	 * @param y                公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters，x或y为{@code null}则返回{@code null}
	 */
	public static ECPublicKeyParameters toPublicParams(String x, String y, ECDomainParameters domainParameters) {
		return toPublicParams(SecureUtil.decode(x), SecureUtil.decode(y), domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param xBytes           公钥X
	 * @param yBytes           公钥Y
	 * @param domainParameters ECDomainParameters曲线参数
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toPublicParams(byte[] xBytes, byte[] yBytes, ECDomainParameters domainParameters) {
		if (null == xBytes || null == yBytes) {
			return null;
		}
		return toPublicParams(BigIntegers.fromUnsignedByteArray(xBytes), BigIntegers.fromUnsignedByteArray(yBytes), domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param x                公钥X
	 * @param y                公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toPublicParams(BigInteger x, BigInteger y, ECDomainParameters domainParameters) {
		if (null == x || null == y) {
			return null;
		}
		final ECCurve curve = domainParameters.getCurve();
		return toPublicParams(curve.createPoint(x, y), domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param pointEncoded     被编码的曲线坐标点
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 * @since 5.4.3
	 */
	public static ECPublicKeyParameters toPublicParams(String pointEncoded, ECDomainParameters domainParameters) {
		final ECCurve curve = domainParameters.getCurve();
		return toPublicParams(curve.decodePoint(SecureUtil.decode(pointEncoded)), domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param pointEncoded     被编码的曲线坐标点
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 * @since 5.4.3
	 */
	public static ECPublicKeyParameters toPublicParams(byte[] pointEncoded, ECDomainParameters domainParameters) {
		final ECCurve curve = domainParameters.getCurve();
		return toPublicParams(curve.decodePoint(pointEncoded), domainParameters);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param point            曲线坐标点
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters
	 * @since 5.4.3
	 */
	public static ECPublicKeyParameters toPublicParams(org.bouncycastle.math.ec.ECPoint point, ECDomainParameters domainParameters) {
		return new ECPublicKeyParameters(point, domainParameters);
	}

	/**
	 * 公钥转换为 {@link ECPublicKeyParameters}
	 *
	 * @param publicKey 公钥，传入null返回null
	 * @return {@link ECPublicKeyParameters}或null
	 */
	public static ECPublicKeyParameters toPublicParams(PublicKey publicKey) {
		if (null == publicKey) {
			return null;
		}
		try {
			return (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}

	//--------------------------------------------------------------------------- Private Key

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值16进制字符串
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(String d) {
		return toPrivateParams(d, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(byte[] d) {
		return toPrivateParams(d, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(BigInteger d) {
		return toPrivateParams(d, SmUtil.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值16进制字符串
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(String d, ECDomainParameters domainParameters) {
		return toPrivateParams(BigIntegers.fromUnsignedByteArray(SecureUtil.decode(d)), domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(byte[] d, ECDomainParameters domainParameters) {
		return toPrivateParams(BigIntegers.fromUnsignedByteArray(d), domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(BigInteger d, ECDomainParameters domainParameters) {
		if (null == d) {
			return null;
		}
		return new ECPrivateKeyParameters(d, domainParameters);
	}

	/**
	 * 私钥转换为 {@link ECPrivateKeyParameters}
	 *
	 * @param privateKey 私钥，传入null返回null
	 * @return {@link ECPrivateKeyParameters}或null
	 */
	public static ECPrivateKeyParameters toPrivateParams(PrivateKey privateKey) {
		if (null == privateKey) {
			return null;
		}
		try {
			return (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey);
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 将SM2算法的{@link ECPrivateKey} 转换为 {@link PrivateKey}
	 *
	 * @param privateKey {@link ECPrivateKey}
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey toSm2PrivateKey(ECPrivateKey privateKey) {
		try {
			final PrivateKeyInfo info = new PrivateKeyInfo(
					new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, SmUtil.ID_SM2_PUBLIC_KEY_PARAM), privateKey);
			return KeyUtil.generatePrivateKey("SM2", info.getEncoded());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建{@link OpenSSHPrivateKeySpec}
	 *
	 * @param key 私钥，需为PKCS#1格式
	 * @return {@link OpenSSHPrivateKeySpec}
	 * @since 5.5.9
	 */
	public static KeySpec createOpenSSHPrivateKeySpec(byte[] key) {
		return new OpenSSHPrivateKeySpec(key);
	}

	/**
	 * 创建{@link OpenSSHPublicKeySpec}
	 *
	 * @param key 公钥，需为PKCS#1格式
	 * @return {@link OpenSSHPublicKeySpec}
	 * @since 5.5.9
	 */
	public static KeySpec createOpenSSHPublicKeySpec(byte[] key) {
		return new OpenSSHPublicKeySpec(key);
	}

	/**
	 * 尝试解析转换各种类型私钥为{@link ECPrivateKeyParameters}，支持包括：
	 *
	 * <ul>
	 *     <li>D值</li>
	 *     <li>PKCS#8</li>
	 *     <li>PKCS#1</li>
	 * </ul>
	 *
	 * @param privateKeyBytes 私钥
	 * @return {@link ECPrivateKeyParameters}
	 * @since 5.5.9
	 */
	public static ECPrivateKeyParameters decodePrivateKeyParams(byte[] privateKeyBytes) {
		try {
			// 尝试D值
			return toSm2PrivateParams(privateKeyBytes);
		} catch (Exception ignore) {
			// ignore
		}

		PrivateKey privateKey;
		//尝试PKCS#8
		try {
			privateKey = KeyUtil.generatePrivateKey("sm2", privateKeyBytes);
		} catch (Exception ignore) {
			// 尝试PKCS#1
			privateKey = KeyUtil.generatePrivateKey("sm2", createOpenSSHPrivateKeySpec(privateKeyBytes));
		}

		return toPrivateParams(privateKey);
	}

	/**
	 * 尝试解析转换各种类型公钥为{@link ECPublicKeyParameters}，支持包括：
	 *
	 * <ul>
	 *     <li>Q值</li>
	 *     <li>X.509</li>
	 *     <li>PKCS#1</li>
	 * </ul>
	 *
	 * @param publicKeyBytes 公钥
	 * @return {@link ECPublicKeyParameters}
	 * @since 5.5.9
	 */
	public static ECPublicKeyParameters decodePublicKeyParams(byte[] publicKeyBytes) {
		try {
			// 尝试Q值
			return toSm2PublicParams(publicKeyBytes);
		} catch (Exception ignore) {
			// ignore
		}

		PublicKey publicKey;
		//尝试X.509
		try {
			publicKey = KeyUtil.generatePublicKey("sm2", publicKeyBytes);
		} catch (Exception ignore) {
			// 尝试PKCS#1
			publicKey = KeyUtil.generatePublicKey("sm2", createOpenSSHPublicKeySpec(publicKeyBytes));
		}

		return toPublicParams(publicKey);
	}
}
