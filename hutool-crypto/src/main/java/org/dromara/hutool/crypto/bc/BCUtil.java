/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;

import java.io.IOException;
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
	 * 只获取私钥里的d，32位字节
	 *
	 * @param privateKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
	 * @return 压缩得到的X
	 * @since 5.1.6
	 */
	public static byte[] encodeECPrivateKey(final PrivateKey privateKey) {
		return ((BCECPrivateKey) privateKey).getD().toByteArray();
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
	 * @param publicKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
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
	 * @param curveName  EC曲线名，例如{@link SmUtil#SM2_DOMAIN_PARAMS}
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(final byte[] encodeByte, final String curveName) {
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
	public static ECDomainParameters toDomainParams(final ECParameterSpec parameterSpec) {
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
	public static ECDomainParameters toDomainParams(final String curveName) {
		return toDomainParams(ECUtil.getNamedCurveByName(curveName));
	}

	/**
	 * 构建ECDomainParameters对象
	 *
	 * @param x9ECParameters {@link X9ECParameters}
	 * @return {@link ECDomainParameters}
	 * @since 5.2.0
	 */
	public static ECDomainParameters toDomainParams(final X9ECParameters x9ECParameters) {
		return new ECDomainParameters(
				x9ECParameters.getCurve(),
				x9ECParameters.getG(),
				x9ECParameters.getN(),
				x9ECParameters.getH()
		);
	}

	/**
	 * Java中的PKCS#8格式私钥转换为OpenSSL支持的PKCS#1格式
	 *
	 * @param privateKey PKCS#8格式私钥
	 * @return PKCS#1格式私钥
	 * @since 5.5.9
	 */
	public static byte[] toPkcs1(final PrivateKey privateKey){
		final PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKey.getEncoded());
		try {
			return pkInfo.parsePrivateKey().toASN1Primitive().getEncoded();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * Java中的X.509格式公钥转换为OpenSSL支持的PKCS#1格式
	 *
	 * @param publicKey X.509格式公钥
	 * @return PKCS#1格式公钥
	 * @since 5.5.9
	 */
	public static byte[] toPkcs1(final PublicKey publicKey){
		final SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo
				.getInstance(publicKey.getEncoded());
		try {
			return spkInfo.parsePublicKey().getEncoded();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
