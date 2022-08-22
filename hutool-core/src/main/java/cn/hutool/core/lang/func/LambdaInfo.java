package cn.hutool.core.lang.func;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 存放lambda信息
 *
 * @author VampireAchao
 */
public class LambdaInfo {

	private static final Type[] EMPTY_TYPE = new Type[0];
	private final Type[] instantiatedMethodParameterTypes;
	private final Type[] parameterTypes;
	private final Type returnType;
	private final String name;
	private final Executable executable;
	private final Class<?> clazz;
	private final SerializedLambda lambda;

	public LambdaInfo(final Executable executable, final SerializedLambda lambda) {
		// return type
		final boolean isMethod = executable instanceof Method;
		final boolean isConstructor = executable instanceof Constructor;
		Assert.isTrue(isMethod || isConstructor, "Unsupported executable type: " + executable.getClass());
		this.returnType = isMethod ?
			((Method)executable).getGenericReturnType() : ((Constructor<?>)executable).getDeclaringClass();

		// lambda info
		this.parameterTypes = executable.getGenericParameterTypes();
		this.name = executable.getName();
		this.clazz = executable.getDeclaringClass();
		this.executable = executable;
		this.lambda = lambda;

		// types
		final int index = lambda.getInstantiatedMethodType().indexOf(";)");
		this.instantiatedMethodParameterTypes = (index > -1) ? getInstantiatedMethodParamTypes(lambda, index) : EMPTY_TYPE;
	}

	/**
	 * 根据lambda对象的方法签名信息，解析获得实际的参数类型
	 */
	private Type[] getInstantiatedMethodParamTypes(SerializedLambda lambda, int index) {
		final String className = lambda.getInstantiatedMethodType().substring(1, index + 1);
		final String[] instantiatedTypeNames = className.split(";");
		final Type[] types = new Type[instantiatedTypeNames.length];
		for (int i = 0; i < instantiatedTypeNames.length; i++) {
			final boolean isArray = instantiatedTypeNames[i].startsWith(StrPool.BRACKET_START);
			if (isArray && !instantiatedTypeNames[i].endsWith(";")) {
				// 如果是数组，需要以 ";" 结尾才能加载
				instantiatedTypeNames[i] += ";";
			} else {
				if (instantiatedTypeNames[i].startsWith("L")) {
					// 如果以 "L" 开头，删除 L
					instantiatedTypeNames[i] = instantiatedTypeNames[i].substring(1);
				}
				if (instantiatedTypeNames[i].endsWith(";")) {
					// 如果以 ";" 结尾，删除 ";"
					instantiatedTypeNames[i] = instantiatedTypeNames[i].substring(0, instantiatedTypeNames[i].length() - 1);
				}
			}
			types[i] = ClassLoaderUtil.loadClass(instantiatedTypeNames[i]);
		}
		return types;
	}

	public Type[] getInstantiatedMethodParameterTypes() {
		return instantiatedMethodParameterTypes;
	}

	public Type[] getParameterTypes() {
		return parameterTypes;
	}

	public Type getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public Executable getExecutable() {
		return executable;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public SerializedLambda getLambda() {
		return lambda;
	}
}
