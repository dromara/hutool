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

package org.dromara.hutool.core.codec.hash.metro;


import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.codec.hash.CityHash;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * https://gitee.com/dromara/hutool/pulls/532
 */
public class MetroHashTest {

	@Test
	public void testEmpty() {
		Assertions.assertEquals("705fb008071e967d", HexUtil.toHex(MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(""))));
	}

	@Test
	public void test1Low() {
		Assertions.assertEquals("AF6F242B7ED32BCB", h64("a"));
	}

	@Test
	public void test1High() {
		Assertions.assertEquals("D51BA21D219C37B3", h64("é"));
	}

	@Test
	public void metroHash64Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long hash64 = MetroHash64.of(0).hash64(str);
		Assertions.assertEquals(147395857347476456L, hash64);
	}

	@Test
	public void metroHash128Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long[] hash128 = MetroHash128.of(0).hash128(str).getLongArray();
		Assertions.assertEquals(228255164667538345L, hash128[0]);
		Assertions.assertEquals(-6394585948993412256L, hash128[1]);
	}

	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Disabled
	public void bulkHashing64Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash64(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Disabled
	public void bulkHashing128Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash128(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash128.of(0).hash128(ByteUtil.toUtf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	private static String[] getRandomStringArray() {
		final String[] result = new String[10000000];
		int index = 0;
		while (index < 10000000) {
			result[index++] = RandomUtil.randomStringLower(RandomUtil.randomInt(64));
		}
		return result;
	}

	private String h64(final String value) {
		return HexUtil.toHex(MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(value))).toUpperCase();
	}
}
