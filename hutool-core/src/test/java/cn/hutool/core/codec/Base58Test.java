package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class Base58Test {
	@Test
	public void testEncode() throws NoSuchAlgorithmException {
		String a = "hello world";
		String encode = Base58.encode(a.getBytes(StandardCharsets.UTF_8));
		Assert.assertEquals("3vQB7B6MrGQZaxCuFg4oh", encode);
	}
	@Test
	public void testEncodePlain() {
		String a = "hello world";
		String encode = Base58.encodePlain(a.getBytes(StandardCharsets.UTF_8));
		Assert.assertEquals("StV1DL6CwTryKyV", encode);
	}
	@Test
	public void testDecode() throws NoSuchAlgorithmException {
		String a = "3vQB7B6MrGQZaxCuFg4oh";
		byte[] decode = Base58.decode(a);
		Assert.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
	@Test
	public void testDecodePlain()  {
		String a = "StV1DL6CwTryKyV";
		byte[] decode = Base58.decodePlain(a);
		Assert.assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8),decode);
	}
}
