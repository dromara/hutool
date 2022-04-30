package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class PercentCodecTest {

	@Test
	public void isSafeTest(){
		PercentCodec codec = PercentCodec.Builder.of("=").build();
		Assert.assertTrue(codec.isSafe('='));

		codec = PercentCodec.Builder.of("=").or(PercentCodec.Builder.of("abc").build()).build();
		Assert.assertTrue(codec.isSafe('a'));
		Assert.assertTrue(codec.isSafe('b'));
		Assert.assertTrue(codec.isSafe('c'));
	}
}
