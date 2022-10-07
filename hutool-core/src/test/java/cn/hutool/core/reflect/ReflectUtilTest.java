package cn.hutool.core.reflect;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

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
		Assert.equals("()I", ReflectUtil.getDescriptor(Object.class.getMethod("hashCode")));
		Assert.equals("()Ljava/lang/String;", ReflectUtil.getDescriptor(Object.class.getMethod("toString")));
		Assert.equals("(Ljava/lang/Object;)Z", ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class)));
		Assert.equals("(II)I", ReflectUtil.getDescriptor(Integer.class.getDeclaredMethod("compare", int.class, int.class)));
		Assert.equals("([Ljava/lang/Object;)Ljava/util/List;", ReflectUtil.getDescriptor(Arrays.class.getMethod("asList", Object[].class)));
		Assert.equals("()V", ReflectUtil.getDescriptor(Object.class.getConstructor()));
		// clazz
		Assert.equals("Z", ReflectUtil.getDescriptor(boolean.class));
		Assert.equals("Ljava/lang/Boolean;", ReflectUtil.getDescriptor(Boolean.class));
		Assert.equals("[[[D", ReflectUtil.getDescriptor(double[][][].class));
		Assert.equals("I", ReflectUtil.getDescriptor(int.class));
		Assert.equals("Ljava/lang/Integer;", ReflectUtil.getDescriptor(Integer.class));
		Assert.equals("V", ReflectUtil.getDescriptor(void.class));
		Assert.equals("Ljava/lang/Void;", ReflectUtil.getDescriptor(Void.class));
		Assert.equals("Ljava/lang/Object;", ReflectUtil.getDescriptor(Object.class));
	}
}
