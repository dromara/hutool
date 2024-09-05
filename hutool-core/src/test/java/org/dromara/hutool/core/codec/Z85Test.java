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

import org.dromara.hutool.core.codec.binary.Z85Codec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Z85Test {
	@Test
	public void testZ85Basic() {
		final byte[] bytes = new byte[]{-122, 79, -46, 111, -75, 89, -9, 91};
		final String string = new Z85Codec().encode(bytes);
		Assertions.assertEquals("HelloWorld", string);
		final byte[] result = new Z85Codec().decode(string);
		Assertions.assertArrayEquals(bytes, result);
	}

	@Test
	public void testZ85Unpadded() {
		final byte[] bytes = new byte[]{0xC, 0x0, 0xF, 0xF, 0xE, 0xE};
		final byte[] result = new Z85Codec().decode(new Z85Codec().encode(bytes));
		Assertions.assertArrayEquals(bytes, result);
	}

	@Test
	public void testZ85UnpaddedShort() {
		final byte[] bytes = new byte[]{0xA, 0xB, 0xC};
		final byte[] result = new Z85Codec().decode(new Z85Codec().encode(bytes));
		Assertions.assertArrayEquals(bytes, result);
	}
}
