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

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

/**
 * SM2常量
 *
 * @author Looly
 * @since 6.0.0
 */
public class SM2Constant {
	/**
	 * SM2默认曲线
	 */
	public static final String SM2_CURVE_NAME = "sm2p256v1";
	/**
	 * SM2椭圆曲线参数类
	 */
	public static final ECParameterSpec SM2_EC_SPEC = ECNamedCurveTable.getParameterSpec(SM2_CURVE_NAME);
	/**
	 * SM2推荐曲线参数（来自https://github.com/ZZMarquis/gmhelper）
	 */
	public static final ECDomainParameters SM2_DOMAIN_PARAMS = BCUtil.toDomainParams(SM2_EC_SPEC);
	/**
	 * SM2国密算法公钥参数的Oid标识
	 */
	public static final ASN1ObjectIdentifier ID_SM2_PUBLIC_KEY_PARAM = new ASN1ObjectIdentifier("1.2.156.10197.1.301");
}
