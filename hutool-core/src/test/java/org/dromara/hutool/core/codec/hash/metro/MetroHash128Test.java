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
import org.dromara.hutool.core.codec.Number128;
import org.dromara.hutool.core.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MetroHash128Test {
	@Test
	public void testEmpty() {
		Assertions.assertEquals("5F3CA3D41D1CB4606B14684C65FB6", h128(""));
	}

	@Test
	public void test1Low() {
		Assertions.assertEquals("E84D9EA70174C3184AC6E55552310F85", h128("a"));
	}

	@Test
	public void test1High() {
		Assertions.assertEquals("9A5BCED4C3CA98CADE13388E3C14C215", h128("é"));
	}

	@Test
	public void test2Palindrome() {
		Assertions.assertEquals("3DDDF558587273E1FD034EC7CC917AC8", h128("aa"));
	}

	@Test
	public void test2() {
		Assertions.assertEquals("458E6A18B65C38AD2552335402A068A2", h128("ab"));
	}

	@Test
	public void test3PalindromeLow() {
		Assertions.assertEquals("19725A6E67A8DD1A84E3C844A20DA938", h128("aaa"));
	}

	@Test
	public void test3PalindromeHigh() {
		Assertions.assertEquals("1DD9CC1D29B5080D5F9F171FB2C50CBB", h128("ééé"));
	}

	@Test
	public void test3() {
		Assertions.assertEquals("89AB9CDB9FAF7BA71CD86385C1F801A5", h128("abc"));
	}

	@Test
	public void test4Palindrome() {
		Assertions.assertEquals("AFD0BBB3764CA0539E46B914B8CB8911", h128("poop"));
	}

	@Test
	public void test4() {
		Assertions.assertEquals("D11B6DB94FE20E3884F3829AD6613D19", h128("fart"));
	}

	@Test
	public void test5() {
		Assertions.assertEquals("D45A3A74885F9C842081929D2E9A3A3B", h128("Hello"));
	}

	@Test
	public void test12() {
		Assertions.assertEquals("A7CEC59B03A9053BA6009EEEC81E81F5", h128("Hello World!"));
	}

	@Test
	public void test31() {
		Assertions.assertEquals("980CA7496A1B26D24E529DFB2B3A870",
				h128("The Quick Brown Fox Jumped Over"));
	}

	@Test
	public void test32() {
		Assertions.assertEquals("76663CEA442E22F86A6CB41FBA896B9B",
				h128("The Quick Brown Fox Jumped Over "));
	}

	@Test
	public void testLonger() {
		Assertions.assertEquals("7010B2D7C8A3515AE3CA4DBBD9ED30D0",
				h128("The Quick Brown Fox Jumped Over The Lazy Dog"));
	}

	static final byte[] ENDIAN =
			ByteUtil.toUtf8Bytes("012345678901234567890123456789012345678901234567890123456789012");

	@Test
	public void testLittleEndian() {
		final ByteBuffer output = ByteBuffer.allocate(16);
		MetroHash128.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.LITTLE_ENDIAN);
		Assertions.assertEquals("C77CE2BFA4ED9F9B0548B2AC5074A297", hex(output.array()));
	}

	@Test
	public void testBigEndian() {
		final ByteBuffer output = ByteBuffer.allocate(16);
		MetroHash128.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.BIG_ENDIAN);
		Assertions.assertEquals("97A27450ACB248059B9FEDA4BFE27CC7", hex(output.array()));
	}

	static String h128(final String input) {
		final MetroHash128 mh = MetroHash128.of(0).apply(ByteBuffer.wrap(ByteUtil.toUtf8Bytes(input)));
		final Number128 hash = mh.get();
		return hex(hash.getHighValue()) + hex(hash.getLowValue());
	}

	private static String hex(final long value){
		return HexUtil.toHex(value).toUpperCase();
	}

	private static String hex(final byte[] bytes){
		return HexUtil.encodeStr(bytes).toUpperCase();
	}
}
