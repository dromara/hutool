package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BooleanUtilTest {

	@Test
	public void toBooleanTest() {
		assertTrue(BooleanUtil.toBoolean("true"));
		assertTrue(BooleanUtil.toBoolean("yes"));
		assertTrue(BooleanUtil.toBoolean("t"));
		assertTrue(BooleanUtil.toBoolean("OK"));
		assertTrue(BooleanUtil.toBoolean("1"));
		assertTrue(BooleanUtil.toBoolean("On"));
		assertTrue(BooleanUtil.toBoolean("是"));
		assertTrue(BooleanUtil.toBoolean("对"));
		assertTrue(BooleanUtil.toBoolean("真"));

		assertFalse(BooleanUtil.toBoolean("false"));
		assertFalse(BooleanUtil.toBoolean("6455434"));
		assertFalse(BooleanUtil.toBoolean(""));
	}

	@Test
	public void andTest(){
		assertFalse(BooleanUtil.and(true,false));
		assertFalse(BooleanUtil.andOfWrap(true,false));
	}

	@Test
	public void orTest(){
		assertTrue(BooleanUtil.or(true,false));
		assertTrue(BooleanUtil.orOfWrap(true,false));
	}

	@Test
	public void xorTest(){
		assertTrue(BooleanUtil.xor(true,false));
		assertTrue(BooleanUtil.xorOfWrap(true,false));
	}

	public void orOfWrapTest() {
		assertFalse(BooleanUtil.orOfWrap(Boolean.FALSE, null));
		assertTrue(BooleanUtil.orOfWrap(Boolean.TRUE, null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isTrueIsFalseTest() {
		assertFalse(BooleanUtil.isTrue(null));
		assertFalse(BooleanUtil.isFalse(null));
	}

	@SuppressWarnings("ConstantConditions")
	public void negateTest() {
		assertFalse(BooleanUtil.negate(Boolean.TRUE));
		assertTrue(BooleanUtil.negate(Boolean.FALSE));

		assertFalse(BooleanUtil.negate(Boolean.TRUE.booleanValue()));
		assertTrue(BooleanUtil.negate(Boolean.FALSE.booleanValue()));
	}

	@Test
	public void toStringTest() {
		assertEquals("true", BooleanUtil.toStringTrueFalse(true));
		assertEquals("false", BooleanUtil.toStringTrueFalse(false));

		assertEquals("yes", BooleanUtil.toStringYesNo(true));
		assertEquals("no", BooleanUtil.toStringYesNo(false));

		assertEquals("on", BooleanUtil.toStringOnOff(true));
		assertEquals("off", BooleanUtil.toStringOnOff(false));
	}

	@Test
	public void issue3587Test() {
		Boolean boolean1 = true;
		Boolean boolean2 = null;
		Boolean result = BooleanUtil.andOfWrap(boolean1, boolean2);
		assertFalse(result);
	}
}
