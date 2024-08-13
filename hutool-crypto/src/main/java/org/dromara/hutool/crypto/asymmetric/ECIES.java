/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.crypto.asymmetric;

import java.security.KeyPair;

/**
 * ECIES（集成加密方案，elliptic curve integrate encrypt scheme）
 *
 * <p>
 * 详细介绍见：https://blog.csdn.net/baidu_26954729/article/details/90437344
 * 此算法必须引入Bouncy Castle库
 *
 * @author loolly
 * @since 5.3.10
 */
public class ECIES extends AsymmetricCrypto{
	private static final long serialVersionUID = 1L;

	/** 默认的ECIES算法 */
	private static final String ALGORITHM_ECIES = "ECIES";

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，生成新的私钥公钥对
	 */
	public ECIES() {
		super(ALGORITHM_ECIES);
	}

	/**
	 * 构造，生成新的私钥公钥对
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 */
	public ECIES(final String eciesAlgorithm) {
		super(eciesAlgorithm);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public ECIES(final String privateKeyStr, final String publicKeyStr) {
		super(ALGORITHM_ECIES, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 * @since 4.5.8
	 */
	public ECIES(final String eciesAlgorithm, final String privateKeyStr, final String publicKeyStr) {
		super(eciesAlgorithm, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public ECIES(final byte[] privateKey, final byte[] publicKey) {
		super(ALGORITHM_ECIES, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param keyPair 密钥对，{@code null}表示随机生成
	 */
	public ECIES(final KeyPair keyPair) {
		super(ALGORITHM_ECIES, keyPair);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 * @param keyPair 密钥对，{@code null}表示随机生成
	 */
	public ECIES(final String eciesAlgorithm, final KeyPair keyPair) {
		super(eciesAlgorithm, keyPair);
	}
	// ------------------------------------------------------------------ Constructor end
}
