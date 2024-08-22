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

package org.dromara.hutool.crypto.digest.mac;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;

/**
 * SM4算法的MAC引擎实现
 *
 * @author looly
 * @since 5.8.0
 */
public class SM4MacEngine extends CBCBlockCipherMacEngine {

	private static final int MAC_SIZE = 128;

	/**
	 * 构造
	 *
	 * @param params {@link CipherParameters}
	 */
	public SM4MacEngine(final CipherParameters params) {
		super(new SM4Engine(), MAC_SIZE, params);
	}
}
