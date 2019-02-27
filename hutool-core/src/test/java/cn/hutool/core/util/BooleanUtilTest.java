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
}
