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

package org.dromara.hutool.core.reflect;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class FieldUtilTest {
	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "privateField");
		Assertions.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = FieldUtil.getFields(ReflectTestBeans.TestSubClass.class);
		Assertions.assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		final ReflectTestBeans.AClass testClass = new ReflectTestBeans.AClass();
		FieldUtil.setFieldValue(testClass, "a", "111");
		Assertions.assertEquals(111, testClass.getA());
	}

	@Test
	public void getDeclaredField() {
		final Field noField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "noField");
		Assertions.assertNull(noField);

		// 获取不到父类字段
		final Field field = FieldUtil.getDeclaredField(ReflectTestBeans.TestSubClass.class, "field");
		Assertions.assertNull(field);

		final Field subField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "subField");
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
