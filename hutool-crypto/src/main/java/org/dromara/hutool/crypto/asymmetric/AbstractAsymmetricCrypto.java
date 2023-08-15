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

package org.dromara.hutool.crypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 抽象的非对称加密对象，包装了加密和解密为Hex和Base64的封装
 *
 * @param <T> 返回自身类型
 * @author Looly
 */
public abstract class AbstractAsymmetricCrypto<T extends AbstractAsymmetricCrypto<T>>
		extends BaseAsymmetric<T>
		implements AsymmetricEncryptor, AsymmetricDecryptor{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public AbstractAsymmetricCrypto(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end
}
