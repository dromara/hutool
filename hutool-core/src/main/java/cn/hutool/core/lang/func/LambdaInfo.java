package cn.hutool.core.lang.func;

import cn.hutool.core.classloader.ClassLoaderUtil;
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

	private final Type[] instantiatedTypes;
	private final Type[] parameterTypes;
	private final Type returnType;
	private final String name;
	private final Executable executable;
	private final Class<?> clazz;
	private final SerializedLambda lambda;

	public LambdaInfo(final Executable executable, final SerializedLambda lambda) {
		if (executable instanceof Method) {
			final Method method = (Method) executable;
			this.parameterTypes = method.getGenericParameterTypes();
			this.returnType = method.getGenericReturnType();
			this.name = method.getName();
			this.clazz = method.getDeclaringClass();
		} else if (executable instanceof Constructor) {
			final Constructor<?> constructor = (Constructor<?>) executable;
			this.parameterTypes = constructor.getGenericParameterTypes();
			this.returnType = constructor.getDeclaringClass();
			this.name = constructor.getName();
			this.clazz = constructor.getDeclaringClass();
		} else {
			throw new IllegalArgumentException("Unsupported executable type: " + executable.getClass());
		}
		final int index = lambda.getInstantiatedMethodType().indexOf(";)");
		if (index > -1) {
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
			this.instantiatedTypes = types;
		} else {
			this.instantiatedTypes = new Type[0];
		}
		this.executable = executable;
		this.lambda = lambda;
	}

	public Type[] getInstantiatedTypes() {
		return instantiatedTypes;
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
