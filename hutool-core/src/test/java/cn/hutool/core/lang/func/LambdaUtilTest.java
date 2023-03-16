package cn.hutool.core.lang.func;

import cn.hutool.core.lang.Tuple;
import cn.hutool.core.reflect.MethodUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest() {
		final SerFunction<MyTeacher, String> lambda = MyTeacher::getAge;
		final String methodName = LambdaUtil.getMethodName(lambda);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		final SerFunction<MyTeacher, String> lambda = MyTeacher::getAge;
		final String fieldName = LambdaUtil.getFieldName(lambda);
		Assert.assertEquals("age", fieldName);
	}

	@Test
	public void resolveTest() {
		Stream.<Runnable>of(() -> {
			// 引用构造函数
			final SerSupplier<MyTeacher> lambda = MyTeacher::new;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(MyTeacher.class, lambdaInfo.getReturnType());
		}, () -> {
			// 数组构造函数引用(此处数组构造参数)
			final SerFunction<Integer, MyTeacher[]> lambda = MyTeacher[]::new;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(int.class, lambdaInfo.getParameterTypes()[0]);
			Assert.assertEquals(MyTeacher[].class, lambdaInfo.getReturnType());
		}, () -> {
			// 引用静态方法
			final SerSupplier<String> lambda = MyTeacher::takeAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 引用特定对象的实例方法
			final SerSupplier<String> lambda = new MyTeacher()::getAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 引用特定类型的任意对象的实例方法
			final SerFunction<MyTeacher, String> lambda = MyTeacher::getAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 最最重要的！！！
			final Character character = '0';
			final Integer integer = 0;
			final SerThiCons<Object, Boolean, String> lambda = (obj, bool, str) -> {
				//noinspection ResultOfMethodCallIgnored
				Objects.nonNull(character);
				//noinspection ResultOfMethodCallIgnored
				Objects.nonNull(integer);
			};
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			// 获取闭包使用的参数类型
			Assert.assertEquals(Character.class, lambdaInfo.getParameterTypes()[0]);
			Assert.assertEquals(Integer.class, lambdaInfo.getParameterTypes()[1]);
			// 最后几个是原有lambda的参数类型
			Assert.assertEquals(Object.class, lambdaInfo.getParameterTypes()[2]);
			Assert.assertEquals(Boolean.class, lambdaInfo.getParameterTypes()[3]);
			Assert.assertEquals(String.class, lambdaInfo.getParameterTypes()[4]);

			Assert.assertEquals(void.class, lambdaInfo.getReturnType());
		}, () -> {
			// 一些特殊的lambda
			Assert.assertEquals("T", LambdaUtil.<SerFunction<Object, Stream<?>>>resolve(Stream::of).getParameterTypes()[0].getTypeName());
			Assert.assertEquals(MyTeacher[][].class, LambdaUtil.<SerFunction<Integer, MyTeacher[][]>>resolve(MyTeacher[][]::new).getReturnType());
			Assert.assertEquals(Integer[][][].class, LambdaUtil.<SerConsumer<Integer[][][]>>resolve(a -> {
			}).getParameterTypes()[0]);
			Assert.assertEquals(Integer[][][].class, LambdaUtil.resolve((Serializable & SerConsumer3<Integer[][][], Integer[][], Integer>) (a, b, c) -> {
			}).getParameterTypes()[0]);
		}).forEach(Runnable::run);

	}

	interface SerThiCons<P1, P2, P3> extends SerConsumer3<P1, P2, P3>, Serializable {
	}

	@Test
	public void getRealClassTest() {
		final MyTeacher myTeacher = new MyTeacher();
		Stream.<Runnable>of(() -> {
			// 引用特定类型的任意对象的实例方法
			final SerFunction<MyTeacher, String> lambda = MyTeacher::getAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 枚举测试，不会导致类型擦除
			final SerFunction<LambdaKindEnum, Integer> lambda = LambdaKindEnum::ordinal;
			Assert.assertEquals(LambdaKindEnum.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 调用父类方法，能获取到正确的子类类型
			final SerFunction<MyTeacher, ?> lambda = MyTeacher::getId;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用特定对象的实例方法
			final SerSupplier<String> lambda = myTeacher::getAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 枚举测试，只能获取到枚举类型
			final SerSupplier<Integer> lambda = LambdaKindEnum.REF_NONE::ordinal;
			Assert.assertEquals(Enum.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 调用父类方法，只能获取到父类类型
			final SerSupplier<?> lambda = myTeacher::getId;
			Assert.assertEquals(Entity.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用静态带参方法，能够获取到正确的参数类型
			final SerFunction<MyTeacher, String> lambda = MyTeacher::takeAgeBy;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用父类静态带参方法，只能获取到父类类型
			final SerSupplier<?> lambda = MyTeacher::takeId;
			Assert.assertEquals(Entity.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用静态无参方法，能够获取到正确的类型
			final SerSupplier<String> lambda = MyTeacher::takeAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用父类静态无参方法，能够获取到正确的参数类型
			final SerFunction<MyTeacher, ?> lambda = MyTeacher::takeIdBy;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 数组测试
			final SerConsumer<String[]> lambda = (final String[] stringList) -> {
			};
			Assert.assertEquals(String[].class, LambdaUtil.getRealClass(lambda));
		}).forEach(Runnable::run);
	}

	@Test
	public void getterTest() {
		final Bean bean = new Bean();
		bean.setId(2L);

		final Function<Bean, Long> getId = LambdaUtil.buildGetter(MethodUtil.getMethod(Bean.class, "getId"));
		final Function<Bean, Long> getId2 = LambdaUtil.buildGetter(Bean.class, Bean.Fields.id);

		Assert.assertEquals(getId, getId2);
		Assert.assertEquals(bean.getId(), getId.apply(bean));
	}

	@Test
	public void setterTest() {
		final Bean bean = new Bean();
		bean.setId(2L);
		bean.setFlag(false);

		final BiConsumer<Bean, Long> setId = LambdaUtil.buildSetter(MethodUtil.getMethod(Bean.class, "setId", Long.class));
		final BiConsumer<Bean, Long> setId2 = LambdaUtil.buildSetter(Bean.class, Bean.Fields.id);
		final BiConsumer<Bean, Boolean> setFlag = LambdaUtil.buildSetter(Bean.class, Bean.Fields.flag);
		Assert.assertEquals(setId, setId2);

		setId.accept(bean, 3L);
		setFlag.accept(bean, true);
		Assert.assertEquals(3L, (long) bean.getId());
		Assert.assertTrue(bean.isFlag());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void lambdaTest() {
		final Bean bean = new Bean();
		bean.setId(1L);
		bean.setPid(0L);
		bean.setFlag(true);
		final BiFunction<Bean, String, Tuple> uniqueKeyFunction = LambdaUtil.build(BiFunction.class, Bean.class, "uniqueKey", String.class);
		final Function4<Tuple, Bean, String, Integer, Double> paramsFunction = LambdaUtil.build(Function4.class, Bean.class, "params", String.class, Integer.class, Double.class);
		Assert.assertEquals(bean.uniqueKey("test"), uniqueKeyFunction.apply(bean, "test"));
		Assert.assertEquals(bean.params("test", 1, 0.5), paramsFunction.apply(bean, "test", 1, 0.5));
	}

	@FunctionalInterface
	interface Function4<R, P1, P2, P3, P4> {
		R apply(P1 p1, P2 p2, P3 p3, P4 p4);
	}

	@Data
	@FieldNameConstants
	private static class Bean {
		Long id;
		Long pid;
		boolean flag;

		@SuppressWarnings("SameParameterValue")
		private Tuple uniqueKey(final String name) {
			return new Tuple(id, pid, flag, name);
		}

		public Tuple params(final String name, final Integer length, final Double score) {
			return new Tuple(name, length, score);
		}

		@SuppressWarnings("unused")
		public static Function<Bean, Long> idGetter() {
			return Bean::getId;
		}

		@SuppressWarnings("unused")
		public Function<Bean, Long> idGet() {
			return bean -> bean.id;
		}

		@SuppressWarnings("unused")
		public Function<Bean, Long> idGetting() {
			return Bean::getId;
		}
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
	@SuppressWarnings("unused")
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
		public static <P> String getLambdaClassName(final Function<P, ?> func) {
			return func.getClass().getName();
		}
	}
}
