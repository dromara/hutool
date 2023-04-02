package org.dromara.hutool.codec;

import org.dromara.hutool.codec.binary.Base58;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class Base58Test {

	@Test
	public void encodeCheckedTest() {
		final String a = "hello world";
		String encode = Base58.encodeChecked(0, a.getBytes());
		Assertions.assertEquals(1 + "3vQB7B6MrGQZaxCuFg4oh", encode);

		// 无版本位
		encode = Base58.encodeChecked(null, a.getBytes());
		Assertions.assertEquals("3vQB7B6MrGQZaxCuFg4oh", encode);
	}
	@Test
	public void encodeTest() {
		final String a = "hello world";
		final String encode = Base58.encode(a.getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals("StV1DL6CwTryKyV", encode);
	}
	@Test
	public void decodeCheckedTest() {
		final String a = "3vQB7B6MrGQZaxCuFg4oh";
		byte[] decode = Base58.decodeChecked(1 + a);
		Assertions.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
		decode = Base58.decodeChecked(a);
		Assertions.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
	@Test
	public void testDecode()  {
		final String a = "StV1DL6CwTryKyV";
		final byte[] decode = Base58.decode(a);
		Assertions.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
}
