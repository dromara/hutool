/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import javax.crypto.Cipher;

/**
 * 密钥类型
 *
 * @author Looly
 *
 */
public enum KeyType {
	/**
	 * 公钥
	 */
	PublicKey(Cipher.PUBLIC_KEY),
	/**
	 * 私钥
	 */
	PrivateKey(Cipher.PRIVATE_KEY),
	/**
	 * 密钥
	 */
	SecretKey(Cipher.SECRET_KEY);


	/**
	 * 构造
	 *
	 * @param value 见{@link Cipher}
	 */
	KeyType(final int value) {
		this.value = value;
	}

	private final int value;

	/**
	 * 获取枚举值对应的int表示
	 *
	 * @return 枚举值对应的int表示
	 */
	public int getValue() {
		return this.value;
	}
}
