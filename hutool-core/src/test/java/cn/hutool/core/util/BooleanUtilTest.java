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
	public void andTest() {
		Assert.assertFalse(BooleanUtil.and(true, false));
		Assert.assertFalse(BooleanUtil.andOfWrap(true, false));
	}

	@Test
	public void orTest() {
		Assert.assertTrue(BooleanUtil.or(true, false));
		Assert.assertTrue(BooleanUtil.orOfWrap(true, false));
	}

	@Test
	public void xorTest() {
		Assert.assertTrue(BooleanUtil.xor(true, false));
		Assert.assertTrue(BooleanUtil.xor(true, true, true));
		Assert.assertFalse(BooleanUtil.xor(true, true, false));
		Assert.assertTrue(BooleanUtil.xor(true, false, false));
		Assert.assertFalse(BooleanUtil.xor(false, false, false));

		Assert.assertTrue(BooleanUtil.xorOfWrap(true, false));
		Assert.assertTrue(BooleanUtil.xorOfWrap(true, true, true));
		Assert.assertFalse(BooleanUtil.xorOfWrap(true, true, false));
		Assert.assertTrue(BooleanUtil.xorOfWrap(true, false, false));
		Assert.assertFalse(BooleanUtil.xorOfWrap(false, false, false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isTrueIsFalseTest() {
		Assert.assertFalse(BooleanUtil.isTrue(null));
		Assert.assertFalse(BooleanUtil.isFalse(null));
	}

	@Test
	public void orOfWrapTest() {
		Assert.assertFalse(BooleanUtil.orOfWrap(Boolean.FALSE, null));
		Assert.assertTrue(BooleanUtil.orOfWrap(Boolean.TRUE, null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void negateTest() {
		Assert.assertFalse(BooleanUtil.negate(Boolean.TRUE));
		Assert.assertTrue(BooleanUtil.negate(Boolean.FALSE));

		Assert.assertFalse(BooleanUtil.negate(Boolean.TRUE.booleanValue()));
		Assert.assertTrue(BooleanUtil.negate(Boolean.FALSE.booleanValue()));
	}

	@Test
	public void toStringTest() {
		Assert.assertEquals("true", BooleanUtil.toStringTrueFalse(true));
		Assert.assertEquals("false", BooleanUtil.toStringTrueFalse(false));

		Assert.assertEquals("yes", BooleanUtil.toStringYesNo(true));
		Assert.assertEquals("no", BooleanUtil.toStringYesNo(false));

		Assert.assertEquals("on", BooleanUtil.toStringOnOff(true));
		Assert.assertEquals("off", BooleanUtil.toStringOnOff(false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void toBooleanObjectTest() {
		Assert.assertTrue(BooleanUtil.toBooleanObject("yes"));
		Assert.assertTrue(BooleanUtil.toBooleanObject("真"));
		Assert.assertTrue(BooleanUtil.toBooleanObject("是"));
		Assert.assertTrue(BooleanUtil.toBooleanObject("√"));

		Assert.assertNull(BooleanUtil.toBooleanObject(null));
		Assert.assertNull(BooleanUtil.toBooleanObject("不识别"));
	}

	@Test
	public void isJsFalsyTest() {
		Assert.assertTrue(BooleanUtil.isJsFalsy(false));
		Assert.assertTrue(BooleanUtil.isJsFalsy(0));
		Assert.assertTrue(BooleanUtil.isJsFalsy(-0));
		Assert.assertTrue(BooleanUtil.isJsFalsy(0L));
		Assert.assertTrue(BooleanUtil.isJsFalsy(0.0D));
		Assert.assertTrue(BooleanUtil.isJsFalsy(0.00D));
		Assert.assertTrue(BooleanUtil.isJsFalsy(-0.00D));
		Assert.assertTrue(BooleanUtil.isJsFalsy(""));
		Assert.assertTrue(BooleanUtil.isJsFalsy(null));
	}

	@Test
	public void isJsTruthyTest() {
		Assert.assertTrue(BooleanUtil.isJsTruthy(true));
		Assert.assertTrue(BooleanUtil.isJsTruthy(1));
		Assert.assertTrue(BooleanUtil.isJsTruthy(-1));
		Assert.assertTrue(BooleanUtil.isJsTruthy("0"));
		Assert.assertTrue(BooleanUtil.isJsTruthy("null"));
		Assert.assertTrue(BooleanUtil.isJsTruthy("undefined"));
		Assert.assertTrue(BooleanUtil.isJsTruthy(1L));
		Assert.assertTrue(BooleanUtil.isJsTruthy(0.1D));
		Assert.assertTrue(BooleanUtil.isJsTruthy(-0.01D));
	}
}
