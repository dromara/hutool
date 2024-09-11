/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierType;
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
	public void hasAnyTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE));
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC)
		);
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.ABSTRACT)
		);
	}

	@Test
	public void hasAllTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assertions.assertTrue(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE));
		Assertions.assertTrue(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC)
		);
		Assertions.assertFalse(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			// 不存在
			ModifierType.ABSTRACT)
		);
	}

	@Test
	void issueIAQ2U0Test() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");

		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC,
			// 不存在
			ModifierType.TRANSIENT
		));

		Assertions.assertFalse(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC,
			// 不存在
			ModifierType.TRANSIENT
		));
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
