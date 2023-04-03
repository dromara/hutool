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
