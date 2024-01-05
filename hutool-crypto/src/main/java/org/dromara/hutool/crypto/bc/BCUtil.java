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
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DefaultBufferedBlockCipher;
import org.bouncycastle.crypto.modes.*;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.crypto.Mode;
import org.dromara.hutool.crypto.Padding;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Bouncy Castle相关工具类封装
 *
 * @author looly
 * @since 4.5.0
 */
public class BCUtil {

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
	public static byte[] toPkcs1(final PrivateKey privateKey) {
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
	public static byte[] toPkcs1(final PublicKey publicKey) {
		final SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo
			.getInstance(publicKey.getEncoded());
		try {
			return spkInfo.parsePublicKey().getEncoded();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 将{@link BlockCipher}包装为指定mode和padding的{@link BufferedBlockCipher}
	 *
	 * @param cipher  {@link BlockCipher}
	 * @param mode    模式
	 * @param padding 补码方式
	 * @return {@link BufferedBlockCipher}，无对应Cipher返回{@code null}
	 * @since 6.0.0
	 */
	public static BufferedBlockCipher wrap(BlockCipher cipher, final Mode mode, final Padding padding) {
		switch (mode) {
			case CBC:
				cipher = CBCBlockCipher.newInstance(cipher);
				break;
			case CFB:
				cipher = CFBBlockCipher.newInstance(cipher, cipher.getBlockSize() * 8);
				break;
			case CTR:
				cipher = SICBlockCipher.newInstance(cipher);
				break;
			case OFB:
				cipher = new OFBBlockCipher(cipher, cipher.getBlockSize() * 8);
			case CTS:
				return new CTSBlockCipher(cipher);
		}

		switch (padding) {
			case NoPadding:
				return new DefaultBufferedBlockCipher(cipher);
			case PKCS5Padding:
				return new PaddedBufferedBlockCipher(cipher);
			case ZeroPadding:
				return new PaddedBufferedBlockCipher(cipher, new ZeroBytePadding());
			case ISO10126Padding:
				return new PaddedBufferedBlockCipher(cipher, new ISO10126d2Padding());
		}

		return null;
	}
}
