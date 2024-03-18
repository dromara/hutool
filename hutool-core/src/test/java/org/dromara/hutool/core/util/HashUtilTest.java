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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.codec.hash.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class HashUtilTest {

	@Test
	public void cityHash128Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long[] hash = HashUtil.cityHash128(ByteUtil.toUtf8Bytes(s)).getLongArray(ByteOrder.BIG_ENDIAN);
		Assertions.assertEquals(0x5944f1e788a18db0L, hash[0]);
		Assertions.assertEquals(0xc2f68d8b2bf4a5cfL, hash[1]);
	}

	@Test
	public void cityHash64Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long hash = HashUtil.cityHash64(ByteUtil.toUtf8Bytes(s));
		Assertions.assertEquals(0x1d408f2bbf967e2aL, hash);
	}

	@Test
	public void cityHash32Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final int hash = HashUtil.cityHash32(ByteUtil.toUtf8Bytes(s));
		Assertions.assertEquals(0xa8944fbe, hash);
	}
}
