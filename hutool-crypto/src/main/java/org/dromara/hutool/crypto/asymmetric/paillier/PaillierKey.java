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

package org.dromara.hutool.crypto.asymmetric.paillier;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Paillier算法密钥
 *
 * @author peterstefanov
 * @author looly
 */
public class PaillierKey implements Key {
	private static final long serialVersionUID = 1L;

	/**
	 * 算法名称：Paillier
	 */
	public static final String ALGORITHM_NAME = "Paillier";

	private final BigInteger n;

	protected PaillierKey(final BigInteger n) {
		this.n = n;
	}

	/**
	 * 获取N值
	 *
	 * @return N值
	 */
	public BigInteger getN() {
		return n;
	}

	@Override
	public String getAlgorithm() {
		return ALGORITHM_NAME;
	}

	@Override
	public String getFormat() {
		return "NONE";
	}

	@Override
	public byte[] getEncoded() {
		return null;
	}

	/**
	 * 获取N * N
	 *
	 * @return N * N
	 */
	public BigInteger getNSquare() {
		return n.multiply(n);
	}

	/**
	 * This method generates a random {@code r} in {@code Z_{n}^*} for
	 * each separate encryption using the same modulus n Paillier cryptosystem
	 * allows the generated r to differ every time, such that the same plaintext
	 * encrypted several times will produce different ciphertext every time.
	 *
	 * @param random {@link SecureRandom}
	 * @return r
	 */
	public BigInteger generateRandomRinZn(final SecureRandom random) {
		BigInteger r;
		// use the same key size as initialised
		final int length = n.bitLength();
		// generate r random integer in Z*_n
		do {
			r = new BigInteger(length, 64, random);
		} while (r.compareTo(n) >= 0 || r.gcd(n).intValue() != 1);

		return r;
	}
}
