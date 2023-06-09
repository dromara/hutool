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

package org.dromara.hutool.crypto.asymmetric.paillier;

import org.dromara.hutool.core.lang.Assert;

import java.math.BigInteger;
import java.security.PrivateKey;

/**
 * Paillier算法公钥
 *
 * @author peterstefanov, Revers, looly
 */
public class PaillierPrivateKey extends PaillierKey implements PrivateKey {
	private static final long serialVersionUID = 1L;

	private final BigInteger u;
	private final BigInteger lambda;

	/**
	 * 构造
	 *
	 * @param n      N值
	 * @param lambda lambda值
	 * @param u      U值
	 */
	public PaillierPrivateKey(final BigInteger n, final BigInteger lambda, final BigInteger u) {
		super(Assert.notNull(n));
		this.lambda = Assert.notNull(lambda);
		this.u = Assert.notNull(u);
	}

	/**
	 * 获取lambda值
	 *
	 * @return lambda值
	 */
	public BigInteger getLambda() {
		return lambda;
	}

	/**
	 * 获取U值
	 *
	 * @return U值
	 */
	public BigInteger getU() {
		return u;
	}
}
