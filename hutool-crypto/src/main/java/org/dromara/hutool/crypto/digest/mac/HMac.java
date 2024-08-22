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

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * HMAC摘要算法<br>
 * HMAC，全称为“Hash Message Authentication Code”，中文名“散列消息鉴别码”<br>
 * 主要是利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。<br>
 * 一般的，消息鉴别码用于验证传输于两个共 同享有一个密钥的单位之间的消息。<br>
 * HMAC 可以与任何迭代散列函数捆绑使用。MD5 和 SHA-1 就是这种散列函数。HMAC 还可以使用一个用于计算和确认消息鉴别值的密钥。<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class HMac extends Mac {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，自动生成密钥
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 */
	public HMac(final HmacAlgorithm algorithm) {
		this(algorithm, (Key) null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(final HmacAlgorithm algorithm, final byte[] key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(final HmacAlgorithm algorithm, final Key key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(final String algorithm, final byte[] key) {
		this(algorithm, new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(final String algorithm, final Key key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @since 5.6.12
	 */
	public HMac(final String algorithm, final Key key, final AlgorithmParameterSpec spec) {
		this(MacEngineFactory.createEngine(algorithm, key, spec));
	}

	/**
	 * 构造
	 *
	 * @param engine MAC算法实现引擎
	 * @since 4.5.13
	 */
	public HMac(final MacEngine engine) {
		super(engine);
	}
	// ------------------------------------------------------------------------------------------- Constructor end
}
