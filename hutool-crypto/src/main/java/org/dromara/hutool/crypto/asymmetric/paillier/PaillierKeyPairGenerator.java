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

import org.dromara.hutool.core.util.RandomUtil;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;

/**
 * Paillier算法密钥对生成器<br>
 * 参考：https://github.com/peterstefanov/paillier
 *
 * @author peterstefanov, looly
 */
public class PaillierKeyPairGenerator extends KeyPairGeneratorSpi {

	private static final int KEYSIZE_DEFAULT = 64;

	/**
	 * 创建PaillierKeyPairGenerator
	 *
	 * @return PaillierKeyPairGenerator
	 */
	public static PaillierKeyPairGenerator of() {
		return of(KEYSIZE_DEFAULT);
	}

	/**
	 * 创建PaillierKeyPairGenerator
	 *
	 * @param keySize 密钥长度，范围：8~3096
	 * @return PaillierKeyPairGenerator
	 */
	public static PaillierKeyPairGenerator of(final int keySize) {
		return of(keySize, null);
	}

	/**
	 * 创建PaillierKeyPairGenerator
	 *
	 * @param keySize 密钥长度，范围：8~3096
	 * @param random  随机对象
	 * @return PaillierKeyPairGenerator
	 */
	public static PaillierKeyPairGenerator of(final int keySize, final SecureRandom random) {
		final PaillierKeyPairGenerator generator = new PaillierKeyPairGenerator();
		generator.initialize(keySize, random);
		return generator;
	}

	private int certainty = 64;
	private int keySize = KEYSIZE_DEFAULT;
	private SecureRandom random;

	@Override
	public void initialize(final int keySize, final SecureRandom random) {
		this.random = random;
		if (keySize < 8 || keySize > 3096) {
			this.keySize = KEYSIZE_DEFAULT;
		} else {
			this.keySize = keySize;
		}
	}

	/**
	 * 设置certainty值，执行时间与此参数的值成比例。
	 *
	 * @param certainty certainty值
	 */
	public void setCertainty(final int certainty) {
		this.certainty = certainty;
	}

	@Override
	public KeyPair generateKeyPair() {
		if (null == this.random) {
			this.random = RandomUtil.getSecureRandom();
		}

		final BigInteger p = generateP();
		final BigInteger q = generateQ(p);

		// n = p*q
		final BigInteger n = p.multiply(q);
		// nsquare = n*n
		final BigInteger nsquare = n.multiply(n);
		final BigInteger lambda = getLambda(p, q);
		final BigInteger g = generateG(n, nsquare, lambda);
		final BigInteger u = getU(g, n, nsquare, lambda);

		final PaillierPublicKey publicKey = new PaillierPublicKey(n, g);
		final PaillierPrivateKey privateKey = new PaillierPrivateKey(n, lambda, u);

		return new KeyPair(publicKey, privateKey);
	}

	// region ----- private methods

	/**
	 * 生成随机P值
	 *
	 * @return P值
	 */
	private BigInteger generateP() {
		return new BigInteger(this.keySize / 2, this.certainty, this.random);
	}

	/**
	 * 生成随机Q值
	 *
	 * @param p P值
	 * @return Q值
	 */
	private BigInteger generateQ(final BigInteger p) {
		BigInteger q;
		do {
			q = new BigInteger(this.keySize / 2, this.certainty, this.random);
		} while (q.compareTo(p) == 0);

		return q;
	}

	/**
	 * 生成G
	 *
	 * @param n       N值
	 * @param nsquare n*n
	 * @param lambda  lambda值
	 * @return G值
	 */
	private BigInteger generateG(final BigInteger n, final BigInteger nsquare, final BigInteger lambda) {
		BigInteger g;
		do {
			// generate g, a random integer in Z*_{n^2}
			do {
				g = new BigInteger(this.keySize, 64, this.random);
			} while (g.compareTo(nsquare) >= 0
				|| g.gcd(nsquare).intValue() != 1);

			// verify g, the following must hold: gcd(L(g^lambda mod n^2), n) =
			// 1,
			// where L(u) = (u-1)/n
		} while (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n)
			.gcd(n).intValue() != 1);

		return g;
	}

	/**
	 * lambda = lcm(p-1, q-1) = (p-1)*(q-1)/gcd(p-1, q-1)
	 *
	 * @param p P值
	 * @param q Q值
	 * @return Lambda
	 */
	private static BigInteger getLambda(final BigInteger p, final BigInteger q) {
		return p.subtract(BigInteger.ONE)
			.multiply(q.subtract(BigInteger.ONE))
			.divide(p.subtract(BigInteger.ONE)
				.gcd(q.subtract(BigInteger.ONE)));
	}

	/**
	 * 获取U（MU）值
	 *
	 * @param g       Q值
	 * @param n       N值
	 * @param nsquare n*n
	 * @param lambda  lambda值
	 * @return U值
	 */
	private static BigInteger getU(final BigInteger g, final BigInteger n, final BigInteger nsquare, final BigInteger lambda) {
		// u = (L(g^lambda mod n^2))^{-1} mod n, where L(u) = (u-1)/n
		return g.modPow(lambda, nsquare)
			.subtract(BigInteger.ONE)
			.divide(n)
			.modInverse(n);
	}
	// endregion
}
