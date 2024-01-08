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

package org.dromara.hutool.crypto.digest.mac;

import org.dromara.hutool.crypto.bc.SmUtil;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * {@link MacEngine} 简单工厂类
 *
 * @author Looly
 * @since 4.5.13
 */
public class MacEngineFactory {

	/**
	 * 根据给定算法和密钥生成对应的{@link MacEngine}
	 *
	 * @param algorithm 算法，见{@link HmacAlgorithm}
	 * @param key       密钥
	 * @return {@link MacEngine}
	 */
	public static MacEngine createEngine(final String algorithm, final Key key) {
		return createEngine(algorithm, key, null);
	}

	/**
	 * 根据给定算法和密钥生成对应的{@link MacEngine}
	 *
	 * @param algorithm 算法，见{@link HmacAlgorithm}
	 * @param key       密钥
	 * @param spec      spec
	 * @return {@link MacEngine}
	 * @since 5.7.12
	 */
	public static MacEngine createEngine(final String algorithm, final Key key, final AlgorithmParameterSpec spec) {
		if (algorithm.equalsIgnoreCase(HmacAlgorithm.HmacSM3.getValue())) {
			// HmacSM3算法是BC库实现的，忽略加盐
			return SmUtil.createHmacSm3Engine(key.getEncoded());
		}
		return new JCEMacEngine(algorithm, key, spec);
	}
}
