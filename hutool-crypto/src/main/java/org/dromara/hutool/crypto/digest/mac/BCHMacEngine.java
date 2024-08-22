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

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * BouncyCastle的HMAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 *
 * @author Looly
 * @since 4.5.13
 */
public class BCHMacEngine extends BCMacEngine {

	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 * @param iv     加盐
	 * @since 5.7.12
	 */
	public BCHMacEngine(final Digest digest, final byte[] key, final byte[] iv) {
		this(digest, new ParametersWithIV(new KeyParameter(key), iv));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 * @since 4.5.13
	 */
	public BCHMacEngine(final Digest digest, final byte[] key) {
		this(digest, new KeyParameter(key));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 4.5.13
	 */
	public BCHMacEngine(final Digest digest, final CipherParameters params) {
		this(new HMac(digest), params);
	}

	/**
	 * 构造
	 *
	 * @param mac {@link HMac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 5.8.0
	 */
	public BCHMacEngine(final HMac mac, final CipherParameters params) {
		super(mac, params);
	}
	// ------------------------------------------------------------------------------------------- Constructor end
}
