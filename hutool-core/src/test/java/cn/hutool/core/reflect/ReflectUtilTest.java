package cn.hutool.core.reflect;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

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
		Assert.equals("()I", ReflectUtil.getDescriptor(Object.class.getMethod("hashCode")));
		Assert.equals("()Ljava/lang/String;", ReflectUtil.getDescriptor(Object.class.getMethod("toString")));
		Assert.equals("(Ljava/lang/Object;)Z", ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class)));
		Assert.equals("(Ljava/lang/Class;Ljava/lang/StringBuilder;)V", ReflectUtil.getDescriptor(ReflectUtil.class.getDeclaredMethod("appendDescriptor", Class.class, StringBuilder.class)));
		Assert.equals("([Ljava/lang/Object;)Z", ReflectUtil.getDescriptor(ArrayUtil.class.getMethod("isEmpty", Object[].class)));
	}
}
