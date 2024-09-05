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

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.crypto.Padding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2613Test {

	@Test
	public void aesGcmTest(){
		final AES aes = new AES("GCM", Padding.NoPadding.name(),
				"1234567890123456".getBytes(),
				"1234567890123456".getBytes());
		final String encryptHex = aes.encryptHex("123456");

		final String s = aes.decryptStr(encryptHex);
		Assertions.assertEquals("123456", s);
	}
}
