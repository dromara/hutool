package cn.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PercentCodecTest {

	@Test
	public void isSafeTest(){
		PercentCodec codec = PercentCodec.Builder.of("=").build();
		Assertions.assertTrue(codec.isSafe('='));

		codec = PercentCodec.Builder.of("=").or(PercentCodec.Builder.of("abc").build()).build();
		Assertions.assertTrue(codec.isSafe('a'));
		Assertions.assertTrue(codec.isSafe('b'));
		Assertions.assertTrue(codec.isSafe('c'));
	}
}
