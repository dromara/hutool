package cn.hutool.core.lang.func;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.reflect.LookupFactory;
import cn.hutool.core.reflect.MethodUtil;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.invoke.LambdaMetafactory.FLAG_SERIALIZABLE;
import static java.lang.invoke.MethodType.methodType;

/**
 * 以类似反射的方式动态创建Lambda，在性能上有一定优势，同时避免每次调用Lambda时创建匿名内部类
 *
 * @author nasodaengineer
 */
public class LambdaFactory {

	private LambdaFactory() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	private static final Map<MutableEntry<Class<?>, Method>, Object> CACHE = new WeakConcurrentMap<>();

	/**
	 * 构建Lambda
	 * <pre>{@code
	 * class Something {
	 *     private Long id;
	 *     private String name;
	 *     // ... 省略GetterSetter方法
	 * }
	 * Function<Something, Long> getIdFunction = LambdaFactory.buildLambda(Function.class, Something.class, "getId");
	 * BiConsumer<Something, String> setNameConsumer = LambdaFactory.buildLambda(BiConsumer.class, Something.class, "setName", String.class);
	 * }
	 * </pre>
	 *
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param methodClass           声明方法的类的类型
	 * @param methodName            方法名称
	 * @param paramTypes            方法参数数组
	 * @return 接受Lambda的函数式接口对象
	 */
	public static <F> F buildLambda(Class<F> functionInterfaceType, Class methodClass, String methodName, Class... paramTypes) {
		return buildLambda(functionInterfaceType, MethodUtil.getMethod(methodClass, methodName, paramTypes));
	}

	/**
	 * 构建Lambda
	 *
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param method                方法对象
	 * @return 接受Lambda的函数式接口对象
	 */
	public static <F> F buildLambda(Class<F> functionInterfaceType, Method method) {
		Assert.notNull(functionInterfaceType);
		Assert.notNull(method);
		MutableEntry<Class<?>, Method> cacheKey = new MutableEntry<>(functionInterfaceType, method);
		//noinspection unchecked
		return (F) CACHE.computeIfAbsent(cacheKey, key -> {
			List<Method> abstractMethods = Arrays.stream(functionInterfaceType.getMethods())
					.filter(m -> Modifier.isAbstract(m.getModifiers()))
					.collect(Collectors.toList());
			Assert.equals(abstractMethods.size(), 1, "不支持非函数式接口");
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			Method invokeMethod = abstractMethods.get(0);
			MethodHandles.Lookup caller = LookupFactory.lookup(method.getDeclaringClass());
			String invokeName = invokeMethod.getName();
			MethodType invokedType = methodType(functionInterfaceType);
			MethodType samMethodType = methodType(invokeMethod.getReturnType(), invokeMethod.getParameterTypes());
			MethodHandle implMethod = Opt.ofTry(() -> caller.unreflect(method)).get();
			MethodType insMethodType = methodType(method.getReturnType(), method.getDeclaringClass(), method.getParameterTypes());
			boolean isSerializable = Arrays.stream(functionInterfaceType.getInterfaces()).anyMatch(i -> i.isAssignableFrom(Serializable.class));
			CallSite callSite = Opt.ofTry(() -> isSerializable ?
					LambdaMetafactory.altMetafactory(
							caller,
							invokeName,
							invokedType,
							samMethodType,
							implMethod,
							insMethodType,
							FLAG_SERIALIZABLE
					) :
					LambdaMetafactory.metafactory(
							caller,
							invokeName,
							invokedType,
							samMethodType,
							implMethod,
							insMethodType
					)).get();

			try {
				//noinspection unchecked
				return (F) callSite.getTarget().invoke();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		});

	}
}
