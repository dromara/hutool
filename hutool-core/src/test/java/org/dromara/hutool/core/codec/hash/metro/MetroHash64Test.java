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
import org.dromara.hutool.core.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 来自：https://github.com/postamar/java-metrohash/blob/master/src/test/java/util/hash/MetroHashTest64.java
 */
public class MetroHash64Test {
	@Test
	public void testEmpty() {
		Assertions.assertEquals("705FB008071E967D", h64(""));
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
	public void test2Palindrome() {
		Assertions.assertEquals("3CF3A8F204CAE1B6", h64("aa"));
	}

	@Test
	public void test2() {
		Assertions.assertEquals("CD2EA2738FC27D98", h64("ab"));
	}

	@Test
	public void test3PalindromeLow() {
		Assertions.assertEquals("E59031D8D046D241", h64("aaa"));
	}

	@Test
	public void test3PalindromeHigh() {
		Assertions.assertEquals("FE8325DC6F40511D", h64("ééé"));
	}

	@Test
	public void test3() {
		Assertions.assertEquals("ED4F5524E6FAFFBB", h64("abc"));
	}

	@Test
	public void test4Palindrome() {
		Assertions.assertEquals("CD77F739885CCB2C", h64("poop"));
	}

	@Test
	public void test4() {
		Assertions.assertEquals("B642DCB026D9573C", h64("fart"));
	}

	@Test
	public void test5() {
		Assertions.assertEquals("A611009FEE6AF8B", h64("Hello"));
	}

	@Test
	public void test12() {
		Assertions.assertEquals("14BCB49B74E3B404", h64("Hello World!"));
	}


	@Test
	public void test31() {
		Assertions.assertEquals("D27A7BFACC320E2F",
				h64("The Quick Brown Fox Jumped Over"));
	}

	@Test
	public void test32() {
		Assertions.assertEquals("C313A3A811EAB43B",
				h64("The Quick Brown Fox Jumped Over "));
	}

	@Test
	public void testLonger() {
		Assertions.assertEquals("C7047C2FCA234C05",
				h64("The Quick Brown Fox Jumped Over The Lazy Dog"));
	}

	static final byte[] ENDIAN =
			ByteUtil.toUtf8Bytes("012345678901234567890123456789012345678901234567890123456789012");

	@Test
	public void testLittleEndian() {
		final ByteBuffer output = ByteBuffer.allocate(8);
		MetroHash64.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.LITTLE_ENDIAN);
		Assertions.assertEquals("6B753DAE06704BAD", hex(output.array()));
	}

	@Test
	public void testBigEndian() {
		final ByteBuffer output = ByteBuffer.allocate(8);
		MetroHash64.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.BIG_ENDIAN);
		Assertions.assertEquals("AD4B7006AE3D756B", hex(output.array()));
	}

	private String h64(final String value) {
		return HexUtil.toHex(MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(value))).toUpperCase();
	}

	private static String hex(final byte[] bytes){
		return HexUtil.encodeStr(bytes).toUpperCase();
	}
}
