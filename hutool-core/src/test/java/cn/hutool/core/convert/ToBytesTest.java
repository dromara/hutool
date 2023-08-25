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

package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ToBytesTest {
	@Test
	public void toBytesTest() {
		final List<Byte> byteList = new ArrayList<>();
		byteList.add((byte) 1);
		byteList.add((byte) 2);
		byteList.add((byte) 3);

		final byte[] bytes = Convert.convert(byte[].class, byteList);
		Assert.assertEquals(1, bytes[0]);
		Assert.assertEquals(2, bytes[1]);
		Assert.assertEquals(3, bytes[2]);
	}
}
