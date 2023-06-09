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

/**
 * 存放Paillier 公钥
 *
 * @author Revers.
 */
public class PaillierpublicKey {
	private BigInteger n;
	private BigInteger g;

	public PaillierpublicKey(BigInteger n, BigInteger g) {
		if (n == null) {
			throw new NullPointerException("n is null");
		}
		if (g == null) {
			throw new NullPointerException("g is null");
		}
		this.n = n;
		this.g = g;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getG() {
		return g;
	}
}
