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
