package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class Base58Test {

	@Test
	public void encodeCheckedTest() {
		String a = "hello world";
		String encode = Base58.encodeChecked(0, a.getBytes());
		Assert.assertEquals(1 + "3vQB7B6MrGQZaxCuFg4oh", encode);

		// 无版本位
		encode = Base58.encodeChecked(null, a.getBytes());
		Assert.assertEquals("3vQB7B6MrGQZaxCuFg4oh", encode);
	}
	@Test
	public void encodeTest() {
		String a = "hello world";
		String encode = Base58.encode(a.getBytes(StandardCharsets.UTF_8));
		Assert.assertEquals("StV1DL6CwTryKyV", encode);
	}
	@Test
	public void decodeCheckedTest() {
		String a = "3vQB7B6MrGQZaxCuFg4oh";
		byte[] decode = Base58.decodeChecked(1 + a);
		Assert.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
		decode = Base58.decodeChecked(a);
		Assert.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
	@Test
	public void testDecode()  {
		String a = "StV1DL6CwTryKyV";
		byte[] decode = Base58.decode(a);
		Assert.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
}
