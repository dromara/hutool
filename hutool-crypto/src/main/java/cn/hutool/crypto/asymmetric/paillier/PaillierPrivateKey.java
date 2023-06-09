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

package cn.hutool.crypto.asymmetric.paillier;

import java.math.BigInteger;

/** 存放Paillier 私钥
 *
 * @author Revers.
 **/
public class PaillierPrivateKey{
	private final BigInteger n;
	private final BigInteger lambda;
	private final BigInteger u;

	public PaillierPrivateKey(BigInteger n, BigInteger lambda, BigInteger u) {
		if (n == null) {
			throw new NullPointerException("n is null");
		}
		if (lambda == null) {
			throw new NullPointerException("lambda is null");
		}
		if (u == null) {
			throw new NullPointerException("u is null");
		}
		this.n = n;
		this.lambda = lambda;
		this.u = u;
	}

	public BigInteger getN() {
		return n;
	}
	public BigInteger getLambda() {
		return lambda;
	}
	public BigInteger getu() {
		return u;
	}

}
