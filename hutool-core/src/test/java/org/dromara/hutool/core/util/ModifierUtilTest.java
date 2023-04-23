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

import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ModifierUtilTest {

	@Test
	public void hasModifierTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assertions.assertTrue(ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.PRIVATE));
		Assertions.assertTrue(ModifierUtil.hasModifier(method,
				ModifierUtil.ModifierType.PRIVATE,
				ModifierUtil.ModifierType.STATIC)
		);
	}

	private static void ddd() {
	}

	@Test
	@EnabledForJreRange(max = org.junit.jupiter.api.condition.JRE.JAVA_8)
	void removeFinalModifyTest() {
		final String fieldName = "DIALECTS";
		final Field field = FieldUtil.getField(JdbcDialects.class, fieldName);
		ModifierUtil.removeFinalModify(field);
	}

	@Test
	@EnabledForJreRange(max = org.junit.jupiter.api.condition.JRE.JAVA_8)
	public void setFinalFieldValueTest() {
		final String fieldName = "DIALECTS";
		final List<Number> dialects =
				Arrays.asList(
						1,
						2,
						3,
						99
				);
		final Field field = FieldUtil.getField(JdbcDialects.class, fieldName);
		ModifierUtil.removeFinalModify(field);
		FieldUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);

		Assertions.assertEquals(dialects, FieldUtil.getFieldValue(JdbcDialects.class, fieldName));
	}

	@SuppressWarnings("unused")
	public static class JdbcDialects {
		private static final List<Number> DIALECTS =
				Arrays.asList(1L, 2L, 3L);
	}
}
