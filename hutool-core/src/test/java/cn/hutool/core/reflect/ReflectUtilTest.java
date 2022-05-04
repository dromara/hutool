package cn.hutool.core.reflect;

import cn.hutool.core.date.Week;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * 反射工具类单元测试
 *
 * @author Looly
 */
public class ReflectUtilTest {

	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = ReflectUtil.getField(TestSubClass.class, "privateField");
		Assert.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = ReflectUtil.getFields(TestSubClass.class);
		Assert.assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		final AClass testClass = new AClass();
		ReflectUtil.setFieldValue(testClass, "a", "111");
		Assert.assertEquals(111, testClass.getA());
	}

	@Test
	public void noneStaticInnerClassTest() {
		final NoneStaticClass testAClass = ReflectUtil.newInstanceIfPossible(NoneStaticClass.class);
		Assert.assertNotNull(testAClass);
		Assert.assertEquals(2, testAClass.getA());
	}

	@Data
	static class AClass {
		private int a;
	}

	@Data
	@SuppressWarnings("InnerClassMayBeStatic")
	class NoneStaticClass {
		private int a = 2;
	}

	@Data
	static class TestBenchClass {
		private int a;
		private String b;
		private String c;
		private String d;
		private String e;
		private String f;
		private String g;
		private String h;
		private String i;
		private String j;
		private String k;
		private String l;
		private String m;
		private String n;
	}

	interface TestInterface1 {
		@SuppressWarnings("unused")
		void getA();

		@SuppressWarnings("unused")
		void getB();

		@SuppressWarnings("unused")
		default void getC() {

		}
	}

	@SuppressWarnings("AbstractMethodOverridesAbstractMethod")
	interface TestInterface2 extends TestInterface1 {
		@Override
		void getB();
	}

	interface TestInterface3 extends TestInterface2 {
		void get3();
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	class C1 implements TestInterface2 {

		@Override
		public void getA() {

		}

		@Override
		public void getB() {

		}
	}

	class C2 extends C1 {
		@Override
		public void getA() {

		}
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ReflectUtil.newInstanceIfPossible(int.class);
		Assert.assertEquals(0, intValue);

		final Integer integer = ReflectUtil.newInstanceIfPossible(Integer.class);
		Assert.assertEquals(new Integer(0), integer);

		final Map<?, ?> map = ReflectUtil.newInstanceIfPossible(Map.class);
		Assert.assertNotNull(map);

		final Collection<?> collection = ReflectUtil.newInstanceIfPossible(Collection.class);
		Assert.assertNotNull(collection);

		final Week week = ReflectUtil.newInstanceIfPossible(Week.class);
		Assert.assertEquals(Week.SUNDAY, week);

		final int[] intArray = ReflectUtil.newInstanceIfPossible(int[].class);
		Assert.assertArrayEquals(new int[0], intArray);
	}

	@Test
	public void getDeclaredField() {
		final Field noField = ReflectUtil.getField(TestSubClass.class, "noField");
		Assert.assertNull(noField);

		// 获取不到父类字段
		final Field field = ReflectUtil.getDeClearField(TestSubClass.class, "field");
		Assert.assertNull(field);

		final Field subField = ReflectUtil.getField(TestSubClass.class, "subField");
		Assert.assertNotNull(subField);
	}

	@SuppressWarnings("unused")
	static class TestClass {
		private String privateField;
		protected String field;

		private void privateMethod() {
		}

		public void publicMethod() {
		}
	}

	@SuppressWarnings({"InnerClassMayBeStatic", "unused"})
	class TestSubClass extends TestClass {
		private String subField;

		private void privateSubMethod() {
		}

		public void publicSubMethod() {
		}

	}
}
