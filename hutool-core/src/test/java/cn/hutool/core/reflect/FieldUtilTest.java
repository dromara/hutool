package cn.hutool.core.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldUtilTest {
	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "privateField");
		Assert.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = FieldUtil.getFields(ReflectUtilTest.TestSubClass.class);
		Assert.assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		final ReflectUtilTest.AClass testClass = new ReflectUtilTest.AClass();
		FieldUtil.setFieldValue(testClass, "a", "111");
		Assert.assertEquals(111, testClass.getA());
	}

	@Test
	public void getDeclaredField() {
		final Field noField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "noField");
		Assert.assertNull(noField);

		// 获取不到父类字段
		final Field field = FieldUtil.getDeClearField(ReflectUtilTest.TestSubClass.class, "field");
		Assert.assertNull(field);

		final Field subField = FieldUtil.getField(ReflectUtilTest.TestSubClass.class, "subField");
		Assert.assertNotNull(subField);
	}
}
