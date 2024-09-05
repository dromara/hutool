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

package org.dromara.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashidsTest {
	@Test
	public void hexEncodeDecode() {
		final Hashids hashids = Hashids.of("my awesome salt".toCharArray());
		final String encoded1 = hashids.encodeFromHex("507f1f77bcf86cd799439011");
		final String encoded2 = hashids.encodeFromHex("0x507f1f77bcf86cd799439011");
		final String encoded3 = hashids.encodeFromHex("0X507f1f77bcf86cd799439011");

		Assertions.assertEquals("R2qnd2vkOJTXm7XV7yq4", encoded1);
		Assertions.assertEquals(encoded1, encoded2);
		Assertions.assertEquals(encoded1, encoded3);
		final String decoded = hashids.decodeToHex(encoded1);
		Assertions.assertEquals("507f1f77bcf86cd799439011", decoded);
	}
}
