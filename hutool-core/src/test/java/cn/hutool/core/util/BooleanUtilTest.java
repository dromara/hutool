package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilTest {
	
	@Test
	public void toBooleanTest() {
		Assert.assertTrue(BooleanUtil.toBoolean("true"));
		Assert.assertTrue(BooleanUtil.toBoolean("yes"));
		Assert.assertTrue(BooleanUtil.toBoolean("t"));
		Assert.assertTrue(BooleanUtil.toBoolean("OK"));
		Assert.assertTrue(BooleanUtil.toBoolean("1"));
		Assert.assertTrue(BooleanUtil.toBoolean("On"));
		Assert.assertTrue(BooleanUtil.toBoolean("是"));
		Assert.assertTrue(BooleanUtil.toBoolean("对"));
		Assert.assertTrue(BooleanUtil.toBoolean("真"));
		
		Assert.assertFalse(BooleanUtil.toBoolean("false"));
		Assert.assertFalse(BooleanUtil.toBoolean("6455434"));
		Assert.assertFalse(BooleanUtil.toBoolean(""));
	}

	@Test
	public void andTest(){
		Assert.assertFalse(BooleanUtil.and(true,false));
		Assert.assertFalse(BooleanUtil.andOfWrap(true,false));
	}

	@Test
	public void orTest(){
		Assert.assertTrue(BooleanUtil.or(true,false));
		Assert.assertTrue(BooleanUtil.orOfWrap(true,false));
	}

	@Test
	public void xorTest(){
		Assert.assertTrue(BooleanUtil.xor(true,false));
		Assert.assertTrue(BooleanUtil.xorOfWrap(true,false));
	}
}
