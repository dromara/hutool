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
