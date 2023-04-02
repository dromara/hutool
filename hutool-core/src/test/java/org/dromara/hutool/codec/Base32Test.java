package org.dromara.hutool.codec;

import org.dromara.hutool.codec.binary.Base32;
import org.dromara.hutool.util.ByteUtil;
import org.dromara.hutool.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base32Test {

	@Test
	public void encodeAndDecodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String encode = Base32.encode(a);
		Assertions.assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI======", encode);

		String decodeStr = Base32.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);

		// 支持小写模式解码
		decodeStr = Base32.decodeStr(encode.toLowerCase());
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void hexEncodeAndDecodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String encode = Base32.encodeHex(ByteUtil.toUtf8Bytes(a));
		Assertions.assertEquals("SIUADPDEMRJ9HBV4N20E9E5AT6EPTPDON3KPBFV7JA2EBBCNSUMADP5OM8======", encode);

		String decodeStr = Base32.decodeStrHex(encode);
		Assertions.assertEquals(a, decodeStr);

		// 支持小写模式解码
		decodeStr = Base32.decodeStrHex(encode.toLowerCase());
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest(){
		final String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		final String encode = Base32.encode(a);
		final String decodeStr = Base32.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void decodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String decodeStr = Base32.decodeStr("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI");
		Assertions.assertEquals(a, decodeStr);
	}
}
