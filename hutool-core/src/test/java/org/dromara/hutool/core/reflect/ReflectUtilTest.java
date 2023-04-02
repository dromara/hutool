package org.dromara.hutool.core.reflect;

import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 反射工具类单元测试
 *
 * @author Looly
 */
public class ReflectUtilTest {

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

	@SuppressWarnings("RedundantMethodOverride")
	class C2 extends C1 {
		@Override
		public void getA() {

		}
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

	@Test
	@SneakyThrows
	public void testGetDescriptor() {
		// methods
		Assertions.assertEquals("()I", ReflectUtil.getDescriptor(Object.class.getMethod("hashCode")));
		Assertions.assertEquals("()Ljava/lang/String;", ReflectUtil.getDescriptor(Object.class.getMethod("toString")));
		Assertions.assertEquals("(Ljava/lang/Object;)Z", ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class)));
		Assertions.assertEquals("(II)I", ReflectUtil.getDescriptor(Integer.class.getDeclaredMethod("compare", int.class, int.class)));
		Assertions.assertEquals("([Ljava/lang/Object;)Ljava/util/List;", ReflectUtil.getDescriptor(Arrays.class.getMethod("asList", Object[].class)));
		Assertions.assertEquals("()V", ReflectUtil.getDescriptor(Object.class.getConstructor()));
		// clazz
		Assertions.assertEquals("Z", ReflectUtil.getDescriptor(boolean.class));
		Assertions.assertEquals("Ljava/lang/Boolean;", ReflectUtil.getDescriptor(Boolean.class));
		Assertions.assertEquals("[[[D", ReflectUtil.getDescriptor(double[][][].class));
		Assertions.assertEquals("I", ReflectUtil.getDescriptor(int.class));
		Assertions.assertEquals("Ljava/lang/Integer;", ReflectUtil.getDescriptor(Integer.class));
		Assertions.assertEquals("V", ReflectUtil.getDescriptor(void.class));
		Assertions.assertEquals("Ljava/lang/Void;", ReflectUtil.getDescriptor(Void.class));
		Assertions.assertEquals("Ljava/lang/Object;", ReflectUtil.getDescriptor(Object.class));
	}
}
