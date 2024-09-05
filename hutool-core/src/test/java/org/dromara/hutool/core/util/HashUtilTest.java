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
