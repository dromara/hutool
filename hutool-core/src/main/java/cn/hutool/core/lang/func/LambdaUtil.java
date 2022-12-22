package cn.hutool.core.lang.func;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;

/**
 * Lambda相关工具类
 *
 * @author looly, Scen
 * @since 5.6.3
 */
public class LambdaUtil {

	private static final WeakConcurrentMap<String, SerializedLambda> cache = new WeakConcurrentMap<>();

	/**
	 * 通过对象的方法或类的静态方法引用，获取lambda实现类
	 * 传入lambda无参数但含有返回值的情况能够匹配到此方法：
	 * <ul>
	 * <li>引用特定对象的实例方法：<pre>{@code
	 * MyTeacher myTeacher = new MyTeacher();
	 * Class<MyTeacher> supplierClass = LambdaUtil.getRealClass(myTeacher::getAge);
	 * Assert.assertEquals(MyTeacher.class, supplierClass);
	 * }</pre></li>
	 * <li>引用静态无参方法：<pre>{@code
	 * Class<MyTeacher> staticSupplierClass = LambdaUtil.getRealClass(MyTeacher::takeAge);
	 * Assert.assertEquals(MyTeacher.class, staticSupplierClass);
	 * }</pre></li>
	 * </ul>
	 * 在以下场景无法获取到正确类型
	 * <pre>{@code
	 * // 枚举测试，只能获取到枚举类型
	 * Class<Enum<?>> enumSupplierClass = LambdaUtil.getRealClass(LambdaUtil.LambdaKindEnum.REF_NONE::ordinal);
	 * Assert.assertEquals(Enum.class, enumSupplierClass);
	 * // 调用父类方法，只能获取到父类类型
	 * Class<Entity<?>> superSupplierClass = LambdaUtil.getRealClass(myTeacher::getId);
	 * Assert.assertEquals(Entity.class, superSupplierClass);
	 * // 引用父类静态带参方法，只能获取到父类类型
	 * Class<Entity<?>> staticSuperFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeId);
	 * Assert.assertEquals(Entity.class, staticSuperFunctionClass);
	 * }</pre>
	 *
	 * @param func lambda
	 * @param <R>  类型
	 * @return lambda实现类
	 * @throws IllegalArgumentException 如果是不支持的方法引用，抛出该异常，见{@link LambdaUtil#checkLambdaTypeCanGetClass}
	 * @since 5.8.0
	 * @author VampireAchao
	 */
	public static <R> Class<R> getRealClass(Func0<?> func) {
		final SerializedLambda lambda = resolve(func);
		checkLambdaTypeCanGetClass(lambda.getImplMethodKind());
		return ClassUtil.loadClass(lambda.getImplClass());
	}

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param <T>  Lambda类型
	 * @param func 需要解析的 lambda 对象（无参方法）
	 * @return 返回解析后的结果
	 */
	public static <T> SerializedLambda resolve(Func1<T, ?> func) {
		return _resolve(func);
	}

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param <R>  Lambda返回类型
	 * @param func 需要解析的 lambda 对象（无参方法）
	 * @return 返回解析后的结果
	 * @since 5.7.23
	 */
	public static <R> SerializedLambda resolve(Func0<R> func) {
		return _resolve(func);
	}

	/**
	 * 获取lambda表达式函数（方法）名称
	 *
	 * @param <P>  Lambda参数类型
	 * @param func 函数（无参方法）
	 * @return 函数名称
	 */
	public static <P> String getMethodName(Func1<P, ?> func) {
		return resolve(func).getImplMethodName();
	}

	/**
	 * 获取lambda表达式函数（方法）名称
	 *
	 * @param <R>  Lambda返回类型
	 * @param func 函数（无参方法）
	 * @return 函数名称
	 * @since 5.7.23
	 */
	public static <R> String getMethodName(Func0<R> func) {
		return resolve(func).getImplMethodName();
	}

	/**
	 * 通过对象的方法或类的静态方法引用，然后根据{@link SerializedLambda#getInstantiatedMethodType()}获取lambda实现类<br>
	 * 传入lambda有参数且含有返回值的情况能够匹配到此方法：
	 * <ul>
	 * <li>引用特定类型的任意对象的实例方法：<pre>{@code
	 * Class<MyTeacher> functionClass = LambdaUtil.getRealClass(MyTeacher::getAge);
	 * Assert.assertEquals(MyTeacher.class, functionClass);
	 * }</pre></li>
	 * <li>引用静态带参方法：<pre>{@code
	 * Class<MyTeacher> staticFunctionClass = LambdaUtil.getRealClass(MyTeacher::takeAgeBy);
	 * Assert.assertEquals(MyTeacher.class, staticFunctionClass);
	 * }</pre></li>
	 * </ul>
	 *
	 * @param func lambda
	 * @param <P>  方法调用方类型
	 * @param <R>  返回值类型
	 * @return lambda实现类
	 * @throws IllegalArgumentException 如果是不支持的方法引用，抛出该异常，见{@link LambdaUtil#checkLambdaTypeCanGetClass}
	 * @since 5.8.0
	 * @author VampireAchao
	 */
	public static <P, R> Class<P> getRealClass(Func1<P, R> func) {
		final SerializedLambda lambda = resolve(func);
		checkLambdaTypeCanGetClass(lambda.getImplMethodKind());
		final String instantiatedMethodType = lambda.getInstantiatedMethodType();
		return ClassUtil.loadClass(StrUtil.sub(instantiatedMethodType, 2, StrUtil.indexOf(instantiatedMethodType, ';')));
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
	 * @param <T>  Lambda类型
	 * @param func 函数（无参方法）
	 * @return 方法名称
	 * @throws IllegalArgumentException 非Getter或Setter方法
	 * @since 5.7.10
	 */
	public static <T> String getFieldName(Func1<T, ?> func) throws IllegalArgumentException {
		return BeanUtil.getFieldName(getMethodName(func));
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
	 * @param <T>  Lambda类型
	 * @param func 函数（无参方法）
	 * @return 方法名称
	 * @throws IllegalArgumentException 非Getter或Setter方法
	 * @since 5.7.23
	 */
	public static <T> String getFieldName(Func0<T> func) throws IllegalArgumentException {
		return BeanUtil.getFieldName(getMethodName(func));
	}

	//region Private methods
	/**
	 * 检查是否为支持的类型
	 *
	 * @param implMethodKind 支持的lambda类型
	 * @throws IllegalArgumentException 如果是不支持的方法引用，抛出该异常
	 */
	private static void checkLambdaTypeCanGetClass(int implMethodKind) {
		if (implMethodKind != MethodHandleInfo.REF_invokeVirtual &&
				implMethodKind != MethodHandleInfo.REF_invokeStatic) {
			throw new IllegalArgumentException("该lambda不是合适的方法引用");
		}
	}

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除。
	 *
	 * <p>
	 * 通过反射调用实现序列化接口函数对象的writeReplace方法，从而拿到{@link SerializedLambda}<br>
	 * 该对象中包含了lambda表达式的所有信息。
	 * </p>
	 *
	 * @param func 需要解析的 lambda 对象
	 * @return 返回解析后的结果
	 */
	private static SerializedLambda _resolve(Serializable func) {
		return cache.computeIfAbsent(func.getClass().getName(), (key) -> ReflectUtil.invoke(func, "writeReplace"));
	}
	//endregion
}
