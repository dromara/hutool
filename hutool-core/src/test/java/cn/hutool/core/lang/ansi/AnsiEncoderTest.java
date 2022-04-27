package cn.hutool.core.lang.ansi;

import org.junit.Assert;
import org.junit.Test;

public class AnsiEncoderTest {

	@Test
	public void encodeTest(){
		final String encode = AnsiEncoder.encode(AnsiColor.GREEN, "Hutool test");
		Assert.assertEquals("\u001B[32mHutool test\u001B[0;39m", encode);
	}
}
