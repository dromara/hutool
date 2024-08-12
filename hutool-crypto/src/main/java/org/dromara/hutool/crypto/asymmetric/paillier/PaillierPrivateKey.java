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
