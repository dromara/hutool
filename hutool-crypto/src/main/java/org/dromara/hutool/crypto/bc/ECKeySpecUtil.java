/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.bouncycastle.jcajce.spec.OpenSSHPrivateKeySpec;
import org.bouncycastle.jcajce.spec.OpenSSHPublicKeySpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;

/**
 * BC密钥规范工具类
 *
 * @author Looly
 * @since 6.0.0
 */
public class ECKeySpecUtil {

	/**
	 * 获取私钥规范
	 *
	 * @param d             私钥D值
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPrivateKeySpec
	 */
	public static ECPrivateKeySpec getPrivateKeySpec(final byte[] d, final ECParameterSpec parameterSpec) {
		return getPrivateKeySpec(BigIntegers.fromUnsignedByteArray(d), parameterSpec);
	}

	/**
	 * 获取私钥规范
	 *
	 * @param d             私钥D值
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPrivateKeySpec
	 */
	public static ECPrivateKeySpec getPrivateKeySpec(final BigInteger d, final ECParameterSpec parameterSpec) {
		return new ECPrivateKeySpec(d, parameterSpec);
	}

	/**
	 * 获取公钥规范
	 *
	 * @param q             公钥Q值
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPublicKeySpec
	 */
	public static ECPublicKeySpec getPublicKeySpec(final byte[] q, final ECParameterSpec parameterSpec) {
		return getPublicKeySpec(parameterSpec.getCurve().decodePoint(q), parameterSpec);
	}

	/**
	 * 获取公钥规范
	 *
	 * @param x             公钥x坐标
	 * @param y             公钥y坐标
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPublicKeySpec
	 */
	public static ECPublicKeySpec getPublicKeySpec(final byte[] x, final byte[] y, final ECParameterSpec parameterSpec) {
		return getPublicKeySpec(
			BigIntegers.fromUnsignedByteArray(x),
			BigIntegers.fromUnsignedByteArray(y),
			parameterSpec);
	}

	/**
	 * 获取公钥规范
	 *
	 * @param x             公钥x坐标
	 * @param y             公钥y坐标
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPublicKeySpec
	 */
	public static ECPublicKeySpec getPublicKeySpec(final BigInteger x, final BigInteger y, final ECParameterSpec parameterSpec) {
		return getPublicKeySpec(parameterSpec.getCurve().createPoint(x, y), parameterSpec);
	}

	/**
	 * 获取公钥规范
	 *
	 * @param ecPoint       公钥坐标
	 * @param parameterSpec {@link ECParameterSpec}
	 * @return ECPublicKeySpec
	 */
	public static ECPublicKeySpec getPublicKeySpec(final ECPoint ecPoint, final ECParameterSpec parameterSpec) {
		return new ECPublicKeySpec(ecPoint, parameterSpec);
	}

	/**
	 * 创建{@link OpenSSHPrivateKeySpec}
	 *
	 * @param key 私钥，需为PKCS#1格式或OpenSSH格式
	 * @return {@link OpenSSHPrivateKeySpec}
	 */
	public static OpenSSHPrivateKeySpec getOpenSSHPrivateKeySpec(final byte[] key) {
		return new OpenSSHPrivateKeySpec(key);
	}

	/**
	 * 创建{@link OpenSSHPublicKeySpec}
	 *
	 * @param key 公钥，需为PKCS#1格式
	 * @return {@link OpenSSHPublicKeySpec}
	 */
	public static OpenSSHPublicKeySpec getOpenSSHPublicKeySpec(final byte[] key) {
		return new OpenSSHPublicKeySpec(key);
	}
}
