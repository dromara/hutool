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
