package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilTest {

	@Test
	public void testVerifyBooleanString(){
		/*true的各种形式*/
		Assert.assertTrue(BooleanUtil.verifyBooleanString("true"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("yes"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("t"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("OK"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("1"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("On"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("是"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("对"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("真"));

		/*false的各种形式*/
		Assert.assertTrue(BooleanUtil.verifyBooleanString("false"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("no"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("n"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("f"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("0"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("off"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("否"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("错"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("假"));
		Assert.assertTrue(BooleanUtil.verifyBooleanString("錯"));

		/*非正常的bool字符串*/
		Assert.assertFalse(BooleanUtil.verifyBooleanString(null));
		Assert.assertFalse(BooleanUtil.verifyBooleanString(""));
		Assert.assertFalse(BooleanUtil.verifyBooleanString("x"));
		Assert.assertFalse(BooleanUtil.verifyBooleanString("a"));
		Assert.assertFalse(BooleanUtil.verifyBooleanString("99"));
		Assert.assertFalse(BooleanUtil.verifyBooleanString("q23"));
	}

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
