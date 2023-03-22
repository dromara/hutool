package cn.hutool.core.lang.func;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.MethodHandleInfo;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest() {
		final String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		final String fieldName = LambdaUtil.getFieldName(MyTeacher::getAge);
		Assert.assertEquals("age", fieldName);
	}

	@Test
	public void resolveTest() {
		// 引用构造函数
		Assert.assertEquals(MethodHandleInfo.REF_newInvokeSpecial,
				LambdaUtil.resolve(MyTeacher::new).getImplMethodKind());
		// 数组构造函数引用
		Assert.assertEquals(MethodHandleInfo.REF_invokeStatic,
				LambdaUtil.resolve(MyTeacher[]::new).getImplMethodKind());
		// 引用静态方法
		Assert.assertEquals(MethodHandleInfo.REF_invokeStatic,
				LambdaUtil.resolve(MyTeacher::takeAge).getImplMethodKind());
		// 引用特定对象的实例方法
		Assert.assertEquals(MethodHandleInfo.REF_invokeVirtual,
				LambdaUtil.resolve(new MyTeacher()::getAge).getImplMethodKind());
		// 引用特定类型的任意对象的实例方法
		Assert.assertEquals(MethodHandleInfo.REF_invokeVirtual,
				LambdaUtil.resolve(MyTeacher::getAge).getImplMethodKind());
	}


	@Test
	public void getRealClassTest() {
		// 引用特定类型的任意对象的实例方法
		final Class<MyTeacher> functionClass = LambdaUtil.getRealClass(MyTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, functionClass);
		// 枚举测试，不会导致类型擦除
		final Class<LambdaKindEnum> enumFunctionClass = LambdaUtil.getRealClass(LambdaKindEnum::ordinal);
		Assert.assertEquals(LambdaKindEnum.class, enumFunctionClass);
		// 调用父类方法，能获取到正确的子类类型
		final Class<MyTeacher> superFunctionClass = LambdaUtil.getRealClass(MyTeacher::getId);
		Assert.assertEquals(MyTeacher.class, superFunctionClass);

		final MyTeacher myTeacher = new MyTeacher();
		// 引用特定对象的实例方法
		final Class<MyTeacher> supplierClass = LambdaUtil.getRealClass(myTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, supplierClass);
		// 枚举测试，只能获取到枚举类型
		final Class<Enum<?>> enumSupplierClass = LambdaUtil.getRealClass(LambdaKindEnum.REF_NONE::ordinal);
		Assert.assertEquals(Enum.class, enumSupplierClass);
		// 调用父类方法，只能获取到父类类型
		final Class<Entity<?>> superSupplierClass = LambdaUtil.getRealClass(myTeacher::getId);
		Assert.assertEquals(Entity.class, superSupplierClass);

		// 引用静态带参方法，能够获取到正确的参数类型
		final Class<MyTeacher> staticFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeAgeBy);
		Assert.assertEquals(MyTeacher.class, staticFunctionClass);
		// 引用父类静态带参方法，只能获取到父类类型
		final Class<Entity<?>> staticSuperFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeId);
		Assert.assertEquals(Entity.class, staticSuperFunctionClass);

		// 引用静态无参方法，能够获取到正确的类型
		final Class<MyTeacher> staticSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeAge);
		Assert.assertEquals(MyTeacher.class, staticSupplierClass);
		// 引用父类静态无参方法，能够获取到正确的参数类型
		final Class<MyTeacher> staticSuperSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeIdBy);
		Assert.assertEquals(MyTeacher.class, staticSuperSupplierClass);
	}

	@Data
	@AllArgsConstructor
	static class MyStudent {

		private String name;
	}

	@Data
	public static class Entity<T> {

		private T id;

		public static <T> T takeId() {
			return new Entity<T>().getId();
		}

		public static <T> T takeIdBy(final Entity<T> entity) {
			return entity.getId();
		}


	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class MyTeacher extends Entity<MyTeacher> {

		public static String takeAge() {
			return new MyTeacher().getAge();
		}

		public static String takeAgeBy(final MyTeacher myTeacher) {
			return myTeacher.getAge();
		}

		public String age;
	}

	/**
	 * 测试Lambda类型枚举
	 */
	enum LambdaKindEnum {
		REF_NONE,
		REF_getField,
		REF_getStatic,
		REF_putField,
		REF_putStatic,
		REF_invokeVirtual,
		REF_invokeStatic,
		REF_invokeSpecial,
		REF_newInvokeSpecial,
	}

	@Test
	public void lambdaClassNameTest() {
		final String lambdaClassName1 = LambdaUtilTestHelper.getLambdaClassName(MyTeacher::getAge);
		final String lambdaClassName2 = LambdaUtilTestHelper.getLambdaClassName(MyTeacher::getAge);
		Assert.assertNotEquals(lambdaClassName1, lambdaClassName2);
	}

	static class LambdaUtilTestHelper {
		public static <P> String getLambdaClassName(final Func1<P, ?> func) {
			return func.getClass().getName();
		}
	}
}
