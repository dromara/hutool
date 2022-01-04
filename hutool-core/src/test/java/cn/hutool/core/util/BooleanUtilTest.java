package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BooleanUtilTest {

	@Test
	public void toBooleanTest() {
		Assertions.assertTrue(BooleanUtil.toBoolean("true"));
		Assertions.assertTrue(BooleanUtil.toBoolean("yes"));
		Assertions.assertTrue(BooleanUtil.toBoolean("t"));
		Assertions.assertTrue(BooleanUtil.toBoolean("OK"));
		Assertions.assertTrue(BooleanUtil.toBoolean("1"));
		Assertions.assertTrue(BooleanUtil.toBoolean("On"));
		Assertions.assertTrue(BooleanUtil.toBoolean("是"));
		Assertions.assertTrue(BooleanUtil.toBoolean("对"));
		Assertions.assertTrue(BooleanUtil.toBoolean("真"));

		Assertions.assertFalse(BooleanUtil.toBoolean("false"));
		Assertions.assertFalse(BooleanUtil.toBoolean("6455434"));
		Assertions.assertFalse(BooleanUtil.toBoolean(""));
	}

	@Test
	public void andTest(){
		Assertions.assertFalse(BooleanUtil.and(true,false));
		Assertions.assertFalse(BooleanUtil.andOfWrap(true,false));
	}

	@Test
	public void orTest(){
		Assertions.assertTrue(BooleanUtil.or(true,false));
		Assertions.assertTrue(BooleanUtil.orOfWrap(true,false));
	}

	@Test
	public void xorTest(){
		Assertions.assertTrue(BooleanUtil.xor(true,false));
		Assertions.assertTrue(BooleanUtil.xorOfWrap(true,false));
	}
}
