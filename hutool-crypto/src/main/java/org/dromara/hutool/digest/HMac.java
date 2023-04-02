/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.digest;

import org.dromara.hutool.digest.mac.Mac;
import org.dromara.hutool.digest.mac.MacEngine;
import org.dromara.hutool.digest.mac.MacEngineFactory;

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
