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

package org.dromara.hutool.core.codec.hash;

import org.dromara.hutool.core.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MurmurHashTest {

	@Test
	public void hash32Test() {
		int hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("你"));
		Assertions.assertEquals(-1898877446, hv);

		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("你好"));
		Assertions.assertEquals(337357348, hv);

		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assertions.assertEquals(1101306141, hv);
		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assertions.assertEquals(-785444229, hv);
	}

	@Test
	public void hash64Test() {
		long hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("你"));
		Assertions.assertEquals(-1349759534971957051L, hv);

		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("你好"));
		Assertions.assertEquals(-7563732748897304996L, hv);

		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assertions.assertEquals(-766658210119995316L, hv);
		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assertions.assertEquals(-7469283059271653317L, hv);
	}
}
