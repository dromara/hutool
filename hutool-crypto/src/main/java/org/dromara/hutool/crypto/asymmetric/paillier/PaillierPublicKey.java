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
import java.security.PublicKey;

/**
 * Paillier算法公钥
 *
 * @author peterstefanov, Revers, looly
 */
public class PaillierPublicKey extends PaillierKey implements PublicKey {
	private static final long serialVersionUID = 1L;

	private final BigInteger g;

	/**
	 * 构造
	 *
	 * @param n N值
	 * @param g G值
	 */
	public PaillierPublicKey(final BigInteger n, final BigInteger g) {
		super(Assert.notNull(n));
		this.g = Assert.notNull(g);
	}

	/**
	 * 获取G值
	 *
	 * @return G值
	 */
	public BigInteger getG() {
		return g;
	}
}
