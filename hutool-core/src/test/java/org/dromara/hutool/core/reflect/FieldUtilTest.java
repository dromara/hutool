/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class FieldUtilTest {
	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "privateField");
		Assertions.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = FieldUtil.getFields(ReflectUtilTest.TestSubClass.class);
		Assertions.assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		final ReflectUtilTest.AClass testClass = new ReflectUtilTest.AClass();
		FieldUtil.setFieldValue(testClass, "a", "111");
		Assertions.assertEquals(111, testClass.getA());
	}

	@Test
	public void getDeclaredField() {
		final Field noField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "noField");
		Assertions.assertNull(noField);

		// 获取不到父类字段
		final Field field = FieldUtil.getDeclearField(ReflectUtilTest.TestSubClass.class, "field");
		Assertions.assertNull(field);

		final Field subField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "subField");
		Assertions.assertNotNull(subField);
	}

	@Test
	void getFieldsValueTest() {
		final TestBean testBean = new TestBean();
		testBean.setA("A");
		testBean.setB(1);

		final Object[] fieldsValue = FieldUtil.getFieldsValue(testBean);
		Assertions.assertEquals(2, fieldsValue.length);
		Assertions.assertEquals("A", fieldsValue[0]);
		Assertions.assertEquals(1, fieldsValue[1]);
	}

	@Test
	void getFieldsValueTest2() {
		final TestBean testBean = new TestBean();
		testBean.setA("A");
		testBean.setB(1);

		final Object[] fieldsValue = FieldUtil.getFieldsValue(testBean, (field ->  field.getName().equals("a")));
		Assertions.assertEquals(1, fieldsValue.length);
		Assertions.assertEquals("A", fieldsValue[0]);
	}

	@Data
	static class TestBean{
		private String a;
		private int b;
	}
}
