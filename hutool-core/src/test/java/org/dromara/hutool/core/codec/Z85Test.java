/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
