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

package org.dromara.hutool.crypto.digest.mac;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.Key;

/**
 * {@link CBCBlockCipherMac}实现的MAC算法，使用CBC Block方式
 *
 * @author looly
 * @since 5.8.0
 */
public class CBCBlockCipherMacEngine extends BCMacEngine {

	/**
	 * 构造
	 *
	 * @param digest        摘要算法，为{@link Digest} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 * @param iv            加盐
	 * @since 5.7.12
	 */
	public CBCBlockCipherMacEngine(final BlockCipher digest, final int macSizeInBits, final Key key, final byte[] iv) {
		this(digest, macSizeInBits, key.getEncoded(), iv);
	}

	/**
	 * 构造
	 *
	 * @param digest        摘要算法，为{@link Digest} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 * @param iv            加盐
	 */
	public CBCBlockCipherMacEngine(final BlockCipher digest, final int macSizeInBits, final byte[] key, final byte[] iv) {
		this(digest, macSizeInBits, new ParametersWithIV(new KeyParameter(key), iv));
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 */
	public CBCBlockCipherMacEngine(final BlockCipher cipher, final int macSizeInBits, final Key key) {
		this(cipher, macSizeInBits, key.getEncoded());
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 */
	public CBCBlockCipherMacEngine(final BlockCipher cipher, final int macSizeInBits, final byte[] key) {
		this(cipher, macSizeInBits, new KeyParameter(key));
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param params        参数，例如密钥可以用{@link KeyParameter}
	 */
	public CBCBlockCipherMacEngine(final BlockCipher cipher, final int macSizeInBits, final CipherParameters params) {
		this(new CBCBlockCipherMac(cipher, macSizeInBits), params);
	}

	/**
	 * 构造
	 *
	 * @param mac    {@link CBCBlockCipherMac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 */
	public CBCBlockCipherMacEngine(final CBCBlockCipherMac mac, final CipherParameters params) {
		super(mac, params);
	}
}
