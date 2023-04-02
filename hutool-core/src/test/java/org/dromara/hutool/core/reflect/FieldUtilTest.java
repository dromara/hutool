package org.dromara.hutool.core.reflect;

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
		final Field field = FieldUtil.getDeClearField(ReflectUtilTest.TestSubClass.class, "field");
		Assertions.assertNull(field);

		final Field subField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "subField");
		Assertions.assertNotNull(subField);
	}
}
