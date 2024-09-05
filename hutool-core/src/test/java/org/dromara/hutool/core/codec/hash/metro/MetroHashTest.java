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
