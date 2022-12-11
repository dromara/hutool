package cn.hutool.core.lang.func;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.reflect.MethodUtil;
import cn.hutool.core.reflect.ReflectUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Lambda相关工具类
 *
 * @author looly, Scen
 * @since 5.6.3
 */
public class LambdaUtil {

	private static final WeakConcurrentMap<Object, LambdaInfo> CACHE = new WeakConcurrentMap<>();

	/**
	 * 通过对象的方法或类的静态方法引用，获取lambda实现类
	 * 传入lambda无参数但含有返回值的情况能够匹配到此方法：
	 * <ul>
	 * 		<li>引用特定对象的实例方法：<pre>{@code
	 * 			MyTeacher myTeacher = new MyTeacher();
	 * 			Class<MyTeacher> supplierClass = LambdaUtil.getRealClass(myTeacher::getAge);
	 * 			Assert.assertEquals(MyTeacher.class, supplierClass);
	 * 			}</pre>
	 * 		</li>
	 * 		<li>引用静态无参方法：<pre>{@code
	 * 			Class<MyTeacher> staticSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeAge);
	 * 			Assert.assertEquals(MyTeacher.class, staticSupplierClass);
	 * 			}</pre>
	 * 		</li>
	 * </ul>
	 * 在以下场景无法获取到正确类型
	 * <pre>{@code
	 * 		// 枚举测试，只能获取到枚举类型
	 * 		Class<Enum<?>> enumSupplierClass = LambdaUtil.getRealClass(LambdaUtil.LambdaKindEnum.REF_NONE::ordinal);
	 * 		Assert.assertEquals(Enum.class, enumSupplierClass);
	 * 		// 调用父类方法，只能获取到父类类型
	 * 		Class<Entity<?>> superSupplierClass = LambdaUtil.getRealClass(myTeacher::getId);
	 * 		Assert.assertEquals(Entity.class, superSupplierClass);
	 * 		// 引用父类静态带参方法，只能获取到父类类型
	 * 		Class<Entity<?>> staticSuperFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeId);
	 * 		Assert.assertEquals(Entity.class, staticSuperFunctionClass);
	 * }</pre>
	 *
	 * @param func lambda
	 * @param <R>  类型
	 * @param <T>  lambda的类型
	 * @return lambda实现类
	 * @author VampireAchao
	 */
	@SuppressWarnings("unchecked")
	public static <R, T extends Serializable> Class<R> getRealClass(final T func) {
		final LambdaInfo lambdaInfo = resolve(func);
		return (Class<R>) Opt.of(lambdaInfo)
				.map(LambdaInfo::getInstantiatedMethodParameterTypes)
				.filter(types -> types.length != 0).map(types -> types[types.length - 1])
				.orElseGet(lambdaInfo::getClazz);
	}

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param func 需要解析的 lambda 对象（无参方法）
	 * @param <T>  lambda的类型
	 * @return 返回解析后的结果
	 */
	public static <T extends Serializable> LambdaInfo resolve(final T func) {
		return CACHE.computeIfAbsent(func, (key) -> {
			final SerializedLambda serializedLambda = _resolve(func);
			final String methodName = serializedLambda.getImplMethodName();
			final Class<?> implClass = ClassLoaderUtil.loadClass(serializedLambda.getImplClass(), true);
			if ("<init>".equals(methodName)) {
				for (final Constructor<?> constructor : implClass.getDeclaredConstructors()) {
					if (ReflectUtil.getDescriptor(constructor).equals(serializedLambda.getImplMethodSignature())) {
						return new LambdaInfo(constructor, serializedLambda);
					}
				}
			} else {
				final Method[] methods = MethodUtil.getMethods(implClass);
				for (final Method method : methods) {
					if (method.getName().equals(methodName)
							&& ReflectUtil.getDescriptor(method).equals(serializedLambda.getImplMethodSignature())) {
						return new LambdaInfo(method, serializedLambda);
					}
				}
			}
			throw new IllegalStateException("No lambda method found.");
		});
	}

	/**
	 * 获取lambda表达式函数（方法）名称
	 *
	 * @param func 函数（无参方法）
	 * @param <T>  lambda的类型
	 * @return 函数名称
	 */
	public static <T extends Serializable> String getMethodName(final T func) {
		return resolve(func).getName();
	}

	/**
	 * 获取lambda表达式Getter或Setter函数（方法）对应的字段名称，规则如下：
	 * <ul>
	 *     <li>getXxxx获取为xxxx，如getName得到name。</li>
	 *     <li>setXxxx获取为xxxx，如setName得到name。</li>
	 *     <li>isXxxx获取为xxxx，如isName得到name。</li>
	 *     <li>其它不满足规则的方法名抛出{@link IllegalArgumentException}</li>
	 * </ul>
	 *
	 * @param func 函数
	 * @param <T> lambda的类型
	 * @return 方法名称
	 * @throws IllegalArgumentException 非Getter或Setter方法
	 * @since 5.7.23
	 */
	public static <T extends Serializable> String getFieldName(final T func) throws IllegalArgumentException {
		return BeanUtil.getFieldName(getMethodName(func));
	}

	/**
	 * 等效于 Obj::getXxx
	 *
	 * @param getMethod getter方法
	 * @param <T> 调用getter方法对象类型
	 * @param <R> getter方法返回值类型
	 * @return Obj::getXxx
	 */
	public static <T, R> Function<T, R> getter(Method getMethod) {
		return LambdaFactory.buildLambda(Function.class, getMethod);
	}

	/**
	 * 等效于 Obj::getXxx
	 *
	 * @param clazz 调用getter方法对象类
	 * @param fieldName 字段名称
	 * @param <T> 调用getter方法对象类型
	 * @param <R> getter方法返回值类型
	 * @return Obj::getXxx
	 */
	public static <T, R> Function<T, R> getter(Class<T> clazz, String fieldName) {
		return LambdaFactory.buildLambda(Function.class, BeanUtil.getBeanDesc(clazz).getGetter(fieldName));
	}

	/**
	 * 等效于 Obj::setXxx
	 *
	 * @param setMethod setter方法
	 * @param <T> 调用setter方法对象类型
	 * @param <P> setter方法返回的值类型
	 * @return Obj::setXxx
	 */
	public static <T, P> BiConsumer<T, P> setter(Method setMethod) {
		return LambdaFactory.buildLambda(BiConsumer.class, setMethod);
	}

	/**
	 * Obj::setXxx
	 *
	 * @param clazz 调用setter方法对象类
	 * @param fieldName 字段名称
	 * @param <T> 调用setter方法对象类型
	 * @param <P> setter方法返回的值类型
	 * @return Obj::setXxx
	 */
	public static <T, P> BiConsumer<T, P> setter(Class<T> clazz, String fieldName) {
		return LambdaFactory.buildLambda(BiConsumer.class, BeanUtil.getBeanDesc(clazz).getSetter(fieldName));
	}

	/**
	 * 等效于 Obj::method
	 *
	 * @param lambdaType 接受lambda的函数式接口类型
	 * @param clazz 调用类
	 * @param methodName 方法名
	 * @param paramsTypes 方法参数类型数组
	 * @param <F> 函数式接口类型
	 * @return Obj::method
	 */
	public static <F> F lambda(Class<F> lambdaType, Class<?> clazz, String methodName, Class... paramsTypes) {
		return LambdaFactory.buildLambda(lambdaType, clazz, methodName, paramsTypes);
	}

	//region Private methods

	/**
	 * 解析lambda表达式,没加缓存
	 *
	 * <p>
	 * 通过反射调用实现序列化接口函数对象的writeReplace方法，从而拿到{@link SerializedLambda}<br>
	 * 该对象中包含了lambda表达式的大部分信息。
	 * </p>
	 *
	 * @param func 需要解析的 lambda 对象
	 * @param <T>  lambda的类型
	 * @return 返回解析后的结果
	 */
	private static <T extends Serializable> SerializedLambda _resolve(final T func) {
		if (func instanceof SerializedLambda) {
			return (SerializedLambda) func;
		}
		if (func instanceof Proxy) {
			throw new IllegalArgumentException("not support proxy, just for now");
		}
		final Class<? extends Serializable> clazz = func.getClass();
		if (!clazz.isSynthetic()) {
			throw new IllegalArgumentException("Not a lambda expression: " + clazz.getName());
		}
		final Object serLambda = MethodUtil.invoke(func, "writeReplace");
		if (serLambda instanceof SerializedLambda) {
			return (SerializedLambda) serLambda;
		}
		throw new UtilException("writeReplace result value is not java.lang.invoke.SerializedLambda");
	}
	//endregion
}
