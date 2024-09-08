/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3728Test {
	@Test
	void sm2Test() {
		final String  publicKey="04beab9c2b800c03263b2d9cfcc832eb6827d5b62dc2ec7f8503c8832799af13b057d6b5bf5bc6c144753f3aa8b6cef8acb00a379a4fbed2f90c546fc2b4586bb0";
		final String  privateKey="3920cfc4828339b34da62b97b44d49d3a9c7dc84d9e6732d4b18f681a339519c";
		final SM2 sm2 = new SM2(privateKey, publicKey);

		final String data = "你好 hutool";
		final byte[] encrypt = sm2.encrypt(data);
		final byte[] decrypt = sm2.decrypt(encrypt);
		Assertions.assertEquals(data, StrUtil.utf8Str(decrypt));
	}
}
