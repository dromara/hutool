/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 椭圆曲线EC(Elliptic Curves)密钥参数相关工具类封装
 *
 * @author looly
 * @since 5.4.3
 */
public class ECKeyUtil {

	/**
	 * 根据私钥参数获取公钥参数
	 *
	 * @param privateKeyParameters 私钥参数
	 * @return 公钥参数
	 * @since 5.5.9
	 */
	public static ECPublicKeyParameters getPublicParams(final ECPrivateKeyParameters privateKeyParameters) {
		final ECDomainParameters domainParameters = privateKeyParameters.getParameters();
		final ECPoint q = new FixedPointCombMultiplier().multiply(domainParameters.getG(), privateKeyParameters.getD());
		return new ECPublicKeyParameters(q, domainParameters);
	}

	/**
	 * 根据私钥获取EC公钥
	 *
	 * @param privateKey EC私钥
	 * @param spec       密钥规范
	 * @return EC公钥
	 */
	public static PublicKey getECPublicKey(final ECPrivateKey privateKey, final ECParameterSpec spec) {
		final org.bouncycastle.jce.spec.ECPublicKeySpec keySpec =
			new org.bouncycastle.jce.spec.ECPublicKeySpec(getQFromD(privateKey.getD(), spec), spec);
		return KeyUtil.generatePublicKey("EC", keySpec);
	}

	/**
	 * 根据私钥D值获取公钥的点坐标(Q值)
	 *
	 * @param d    私钥d值
	 * @param spec 密钥规范
	 * @return 公钥的点坐标
	 */
	public static ECPoint getQFromD(final BigInteger d, final ECParameterSpec spec) {
		return spec.getG().multiply(d).normalize();
	}

	// region ----- encode and decode

	/**
	 * 只获取私钥里的d，32位字节
	 *
	 * @param privateKey {@link PublicKey}，必须为org.bouncycastle.jce.interfaces.ECPrivateKey
	 * @return 压缩得到的X
	 * @since 5.1.6
	 */
	public static byte[] encodeECPrivateKey(final PrivateKey privateKey) {
		return ((ECPrivateKey) privateKey).getD().toByteArray();
	}

	/**
	 * 编码压缩EC公钥（基于BouncyCastle），即Q值<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param publicKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
	 * @return 压缩得到的Q
	 * @since 4.4.4
	 */
	public static byte[] encodeECPublicKey(final PublicKey publicKey) {
		return encodeECPublicKey(publicKey, true);
	}

	/**
	 * 编码压缩EC公钥（基于BouncyCastle），即Q值<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param publicKey    {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
	 * @param isCompressed 是否压缩
	 * @return 得到的Q
	 * @since 5.5.9
	 */
	public static byte[] encodeECPublicKey(final PublicKey publicKey, final boolean isCompressed) {
		return ((BCECPublicKey) publicKey).getQ().getEncoded(isCompressed);
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
	public static PublicKey decodeECPoint(final String encode, final String curveName) {
		return decodeECPoint(SecureUtil.decode(encode), curveName);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）
	 *
	 * @param encodeByte 压缩公钥
	 * @param curveName  EC曲线名，例如{@link SM2Constant#SM2_DOMAIN_PARAMS}
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(final byte[] encodeByte, final String curveName) {
		final X9ECParameters x9ECParameters = ECUtil.getNamedCurveByName(curveName);
		final ECCurve curve = x9ECParameters.getCurve();
		final java.security.spec.ECPoint point = EC5Util.convertPoint(curve.decodePoint(encodeByte));

		// 根据曲线恢复公钥格式
		final ECNamedCurveSpec ecSpec = new ECNamedCurveSpec(curveName, curve, x9ECParameters.getG(), x9ECParameters.getN());
		return KeyUtil.generatePublicKey("EC", new ECPublicKeySpec(point, ecSpec));
	}
	// endregion

	/**
	 * 密钥转换为AsymmetricKeyParameter
	 *
	 * @param key PrivateKey或者PublicKey
	 * @return ECPrivateKeyParameters或者ECPublicKeyParameters
	 */
	public static AsymmetricKeyParameter toParams(final Key key) {
		if (key instanceof PrivateKey) {
			return toPrivateParams((PrivateKey) key);
		} else if (key instanceof PublicKey) {
			return toPublicParams((PublicKey) key);
		}

		return null;
	}

	// region ----- toXXPublicParams

	/**
	 * 转换为 ECPublicKeyParameters
	 *
	 * @param q 公钥Q值
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(final byte[] q) {
		return toPublicParams(q, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPublicKeyParameters
	 *
	 * @param q 公钥Q值
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(final String q) {
		return toPublicParams(q, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param x 公钥X
	 * @param y 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(final String x, final String y) {
		return toPublicParams(x, y, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为SM2的ECPublicKeyParameters
	 *
	 * @param xBytes 公钥X
	 * @param yBytes 公钥Y
	 * @return ECPublicKeyParameters
	 */
	public static ECPublicKeyParameters toSm2PublicParams(final byte[] xBytes, final byte[] yBytes) {
		return toPublicParams(xBytes, yBytes, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为ECPublicKeyParameters
	 *
	 * @param x                公钥X
	 * @param y                公钥Y
	 * @param domainParameters ECDomainParameters
	 * @return ECPublicKeyParameters，x或y为{@code null}则返回{@code null}
	 */
	public static ECPublicKeyParameters toPublicParams(final String x, final String y, final ECDomainParameters domainParameters) {
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
	public static ECPublicKeyParameters toPublicParams(final byte[] xBytes, final byte[] yBytes, final ECDomainParameters domainParameters) {
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
	public static ECPublicKeyParameters toPublicParams(final BigInteger x, final BigInteger y, final ECDomainParameters domainParameters) {
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
	public static ECPublicKeyParameters toPublicParams(final String pointEncoded, final ECDomainParameters domainParameters) {
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
	public static ECPublicKeyParameters toPublicParams(final byte[] pointEncoded, final ECDomainParameters domainParameters) {
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
	public static ECPublicKeyParameters toPublicParams(final org.bouncycastle.math.ec.ECPoint point, final ECDomainParameters domainParameters) {
		return new ECPublicKeyParameters(point, domainParameters);
	}

	/**
	 * 公钥转换为 {@link ECPublicKeyParameters}
	 *
	 * @param publicKey 公钥，传入null返回null
	 * @return {@link ECPublicKeyParameters}或null
	 */
	public static ECPublicKeyParameters toPublicParams(final PublicKey publicKey) {
		if (null == publicKey) {
			return null;
		}
		try {
			return (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
		} catch (final InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}
	// endreion


	// region ----- toXXPrivateParams

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值16进制字符串
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(final String d) {
		return toPrivateParams(d, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(final byte[] d) {
		return toPrivateParams(d, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d 私钥d值
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toSm2PrivateParams(final BigInteger d) {
		return toPrivateParams(d, SM2Constant.SM2_DOMAIN_PARAMS);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值16进制字符串
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(final String d, final ECDomainParameters domainParameters) {
		if (null == d) {
			return null;
		}
		return toPrivateParams(
			BigIntegers.fromUnsignedByteArray(Objects.requireNonNull(SecureUtil.decode(d))),
			domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(final byte[] d, final ECDomainParameters domainParameters) {
		if (null == d) {
			return null;
		}
		return toPrivateParams(BigIntegers.fromUnsignedByteArray(d), domainParameters);
	}

	/**
	 * 转换为 ECPrivateKeyParameters
	 *
	 * @param d                私钥d值
	 * @param domainParameters ECDomainParameters
	 * @return ECPrivateKeyParameters
	 */
	public static ECPrivateKeyParameters toPrivateParams(final BigInteger d, final ECDomainParameters domainParameters) {
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
	public static ECPrivateKeyParameters toPrivateParams(final PrivateKey privateKey) {
		if (null == privateKey) {
			return null;
		}
		try {
			return (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey);
		} catch (final InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}
	// endregion

	// region ----- 生成密钥 generateXXKey

	/**
	 * 将SM2算法的{@link ASN1Encodable}格式私钥 生成为 {@link PrivateKey}
	 *
	 * @param privateKey {@link ASN1Encodable}格式的私钥
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(final ASN1Encodable privateKey) {
		try {
			final PrivateKeyInfo info = new PrivateKeyInfo(
				new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, SM2Constant.ID_SM2_PUBLIC_KEY_PARAM), privateKey);
			return KeyUtil.generatePrivateKey("SM2", info.getEncoded());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 生成SM2私钥，支持包括：
	 *
	 * <ul>
	 *     <li>D值</li>
	 *     <li>PKCS#8</li>
	 *     <li>PKCS#1</li>
	 *     <li>OpenSSH格式</li>
	 * </ul>
	 *
	 * @param privateKeyBytes 私钥
	 * @return {@link ECPrivateKeyParameters}
	 */
	public static PrivateKey generateSm2PrivateKey(final byte[] privateKeyBytes) {
		if (null == privateKeyBytes) {
			return null;
		}
		final String algorithm = "SM2";
		KeySpec keySpec;
		// 尝试D值
		try {
			keySpec = ECKeySpecUtil.getPrivateKeySpec(privateKeyBytes, SM2Constant.SM2_EC_SPEC);
			return KeyUtil.generatePrivateKey(algorithm, keySpec);
		} catch (final Exception ignore) {
		}

		//尝试PKCS#8
		try {
			keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			return KeyUtil.generatePrivateKey(algorithm, keySpec);
		} catch (final Exception ignore) {
		}

		// 尝试PKCS#1或OpenSSH格式
		keySpec = ECKeySpecUtil.getOpenSSHPrivateKeySpec(privateKeyBytes);
		return KeyUtil.generatePrivateKey(algorithm, keySpec);
	}

	/**
	 * 生成SM2公钥，支持包括：
	 *
	 * <ul>
	 *     <li>Q值</li>
	 *     <li>X.509</li>
	 *     <li>PKCS#1</li>
	 * </ul>
	 *
	 * @param publicKeyBytes 公钥
	 * @return {@link ECPublicKeyParameters}
	 */
	public static PublicKey generateSm2PublicKey(final byte[] publicKeyBytes) {
		if (null == publicKeyBytes) {
			return null;
		}
		final String algorithm = "SM2";
		KeySpec keySpec;
		// 尝试Q值
		try {
			keySpec = ECKeySpecUtil.getPublicKeySpec(publicKeyBytes, SM2Constant.SM2_EC_SPEC);
			return KeyUtil.generatePublicKey(algorithm, keySpec);
		} catch (final Exception ignore) {
			// ignore
		}

		//尝试X.509
		try {
			keySpec = new X509EncodedKeySpec(publicKeyBytes);
			return KeyUtil.generatePublicKey(algorithm, keySpec);
		} catch (final Exception ignore) {
		}

		// 尝试PKCS#1
		keySpec = ECKeySpecUtil.getOpenSSHPublicKeySpec(publicKeyBytes);
		return KeyUtil.generatePublicKey(algorithm, keySpec);
	}

	/**
	 * 尝试解析转换各种类型公钥为{@link ECPublicKeyParameters}，支持包括：
	 *
	 * @param x 坐标X
	 * @param y 坐标y
	 * @return {@link ECPublicKeyParameters}
	 */
	public static PublicKey generateSm2PublicKey(final byte[] x, final byte[] y) {
		if (null == x || null == y) {
			return null;
		}
		return KeyUtil.generatePublicKey("sm2",
			ECKeySpecUtil.getPublicKeySpec(x, y, SM2Constant.SM2_EC_SPEC));
	}
	// endregion
}
