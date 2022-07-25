package cn.hutool.core.lang.func;

import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.invoke.MethodHandleInfo;
import java.time.LocalDate;
import java.util.*;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest() {
		String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		String fieldName = LambdaUtil.getFieldName(MyTeacher::getAge);
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
		Class<MyTeacher> functionClass = LambdaUtil.getRealClass(MyTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, functionClass);
		// 枚举测试，不会导致类型擦除
		Class<LambdaKindEnum> enumFunctionClass = LambdaUtil.getRealClass(LambdaKindEnum::ordinal);
		Assert.assertEquals(LambdaKindEnum.class, enumFunctionClass);
		// 调用父类方法，能获取到正确的子类类型
		Class<MyTeacher> superFunctionClass = LambdaUtil.getRealClass(MyTeacher::getId);
		Assert.assertEquals(MyTeacher.class, superFunctionClass);

		MyTeacher myTeacher = new MyTeacher();
		// 引用特定对象的实例方法
		Class<MyTeacher> supplierClass = LambdaUtil.getRealClass(myTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, supplierClass);
		// 枚举测试，只能获取到枚举类型
		Class<Enum<?>> enumSupplierClass = LambdaUtil.getRealClass(LambdaKindEnum.REF_NONE::ordinal);
		Assert.assertEquals(Enum.class, enumSupplierClass);
		// 调用父类方法，只能获取到父类类型
		Class<Entity<?>> superSupplierClass = LambdaUtil.getRealClass(myTeacher::getId);
		Assert.assertEquals(Entity.class, superSupplierClass);

		// 引用静态带参方法，能够获取到正确的参数类型
		Class<MyTeacher> staticFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeAgeBy);
		Assert.assertEquals(MyTeacher.class, staticFunctionClass);
		// 引用父类静态带参方法，只能获取到父类类型
		Class<Entity<?>> staticSuperFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeId);
		Assert.assertEquals(Entity.class, staticSuperFunctionClass);

		// 引用静态无参方法，能够获取到正确的类型
		Class<MyTeacher> staticSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeAge);
		Assert.assertEquals(MyTeacher.class, staticSupplierClass);
		// 引用父类静态无参方法，能够获取到正确的参数类型
		Class<MyTeacher> staticSuperSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeIdBy);
		Assert.assertEquals(MyTeacher.class, staticSuperSupplierClass);
	}

	@Test
	public void callVoidFuncTest() {
		StringBuilder result = new StringBuilder();
		Integer params = 3;
		//将无参函数的执行代码put到Map中，用于动态匹配执行 (可替换过多的if else判断)
		Map<Integer, VoidFunc0> funcMap = new HashMap<>();
		funcMap.put(1, () -> result.append("A"));
		funcMap.put(2, () -> result.append("B"));
		funcMap.put(3, () -> result.append("C"));

		//forEach匹配要执行的无参函数
		funcMap.forEach((key, value) -> {
			boolean isCall = Objects.equals(key, params);
			LambdaUtil.callVoidFunc(isCall, value);
		});
		Assert.assertEquals("C", result.toString());
	}

	@Test
	public void callVoidReturnFuncTest() {
		Integer params = 1;
		//将无参函数的执行代码put到Map中，并设置类型为LocalDate的返回值
		Map<Integer, Func0 <LocalDate> > funcMap = new HashMap<>();
		funcMap.put(1, () ->  LocalDate.of(2022,7,26) );
		funcMap.put(2, () -> LocalDate.of(2021,7,26));
		funcMap.put(3, () -> LocalDate.of(2020,7,26));

		//forEach匹配要执行的函数,并获取正确的LocalDate 返回值
		Optional < LocalDate> resultOptional = funcMap.entrySet().stream().
				filter(entry -> Objects.equals(entry.getKey(), params)).
				map(entry-> LambdaUtil.callVoidReturnFunc(true, entry.getValue())).findAny();

		LocalDate result =  resultOptional.orElse(null);
		Assert.assertEquals(2022, result.getYear());
		Assert.assertEquals(7, result.getMonthValue());
		Assert.assertEquals(26, result.getDayOfMonth());
	}

	@Test
	public void callParameterReturnFuncTest() {
		Integer paramsIndex = 1;
		String params = "test";
		//将有参函数的执行代码put到Map中，并设置三个不同的返回值
		Map<Integer, Func1 <String,Object> > funcMap = new HashMap<>();
		funcMap.put(1, str ->  Integer.MAX_VALUE);
		funcMap.put(2, str -> "Success");
		funcMap.put(3, str -> LocalDate.of(2020,7,26));

		//forEach匹配要执行的函数,并获取Object的返回值
		Optional <Object> resultOptional = funcMap.entrySet().stream().
				filter(entry -> Objects.equals(entry.getKey(), paramsIndex)).
				map(entry-> LambdaUtil.callParameterReturnFunc(true, params,entry.getValue())).findAny();
		Object result = resultOptional.orElse(null);
		Assert.assertEquals(Integer.MAX_VALUE, result);
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

		public static <T> T takeIdBy(Entity<T> entity) {
			return entity.getId();
		}


	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class MyTeacher extends Entity<MyTeacher> {

		public static String takeAge() {
			return new MyTeacher().getAge();
		}

		public static String takeAgeBy(MyTeacher myTeacher) {
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
}
