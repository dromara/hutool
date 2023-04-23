/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

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
	public void andTest() {
		Assertions.assertFalse(BooleanUtil.and(true, false));
		Assertions.assertFalse(BooleanUtil.andOfWrap(true, false));
	}

	@Test
	public void orTest() {
		Assertions.assertTrue(BooleanUtil.or(true, false));
		Assertions.assertTrue(BooleanUtil.orOfWrap(true, false));
	}

	@Test
	public void xorTest() {
		Assertions.assertTrue(BooleanUtil.xor(true, false));
		Assertions.assertTrue(BooleanUtil.xor(true, true, true));
		Assertions.assertFalse(BooleanUtil.xor(true, true, false));
		Assertions.assertTrue(BooleanUtil.xor(true, false, false));
		Assertions.assertFalse(BooleanUtil.xor(false, false, false));

		Assertions.assertTrue(BooleanUtil.xorOfWrap(true, false));
		Assertions.assertTrue(BooleanUtil.xorOfWrap(true, true, true));
		Assertions.assertFalse(BooleanUtil.xorOfWrap(true, true, false));
		Assertions.assertTrue(BooleanUtil.xorOfWrap(true, false, false));
		Assertions.assertFalse(BooleanUtil.xorOfWrap(false, false, false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isTrueIsFalseTest() {
		Assertions.assertFalse(BooleanUtil.isTrue(null));
		Assertions.assertFalse(BooleanUtil.isFalse(null));
	}

	@Test
	public void orOfWrapTest() {
		Assertions.assertFalse(BooleanUtil.orOfWrap(Boolean.FALSE, null));
		Assertions.assertTrue(BooleanUtil.orOfWrap(Boolean.TRUE, null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void negateTest() {
		Assertions.assertFalse(BooleanUtil.negate(Boolean.TRUE));
		Assertions.assertTrue(BooleanUtil.negate(Boolean.FALSE));

		Assertions.assertFalse(BooleanUtil.negate(Boolean.TRUE.booleanValue()));
		Assertions.assertTrue(BooleanUtil.negate(Boolean.FALSE.booleanValue()));
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("true", BooleanUtil.toStringTrueFalse(true));
		Assertions.assertEquals("false", BooleanUtil.toStringTrueFalse(false));

		Assertions.assertEquals("yes", BooleanUtil.toStringYesNo(true));
		Assertions.assertEquals("no", BooleanUtil.toStringYesNo(false));

		Assertions.assertEquals("on", BooleanUtil.toStringOnOff(true));
		Assertions.assertEquals("off", BooleanUtil.toStringOnOff(false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void toBooleanObjectTest(){
		Assertions.assertTrue(BooleanUtil.toBooleanObject("yes"));
		Assertions.assertTrue(BooleanUtil.toBooleanObject("真"));
		Assertions.assertTrue(BooleanUtil.toBooleanObject("是"));
		Assertions.assertTrue(BooleanUtil.toBooleanObject("√"));

		Assertions.assertNull(BooleanUtil.toBooleanObject(null));
		Assertions.assertNull(BooleanUtil.toBooleanObject("不识别"));
	}
}
