package cn.hutool.core.lang.func;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.reflect.FieldUtil;
import cn.hutool.core.text.StrUtil;

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

	private Type instantiatedType;
	private final Type[] parameterTypes;
	private final Type returnType;
	private final String name;
	private final Executable executable;
	private final Class<?> clazz;
	private final SerializedLambda lambda;

	public LambdaInfo(Executable executable, SerializedLambda lambda) {
		if (executable instanceof Method) {
			Method method = (Method) executable;
			this.parameterTypes = method.getGenericParameterTypes();
			this.returnType = method.getGenericReturnType();
			this.name = method.getName();
		} else if (executable instanceof Constructor) {
			Constructor<?> constructor = (Constructor<?>) executable;
			this.parameterTypes = constructor.getGenericParameterTypes();
			this.returnType = constructor.getDeclaringClass();
			this.name = constructor.getName();
		} else {
			throw new IllegalArgumentException("Unsupported executable type: " + executable.getClass());
		}
		int index = lambda.getInstantiatedMethodType().indexOf(";)");
		if (index > -1) {
			this.instantiatedType = ClassLoaderUtil.loadClass(StrUtil.sub(lambda.getInstantiatedMethodType(), 2, index));
		}
		this.clazz = (Class<?>) FieldUtil.getFieldValue(executable, "clazz");
		this.executable = executable;
		this.lambda = lambda;
	}

	public Type getInstantiatedType() {
		return instantiatedType;
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
