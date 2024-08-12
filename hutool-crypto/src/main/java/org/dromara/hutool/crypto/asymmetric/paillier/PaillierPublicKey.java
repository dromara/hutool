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
