package cn.hutool.core.lang.func;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.reflect.FieldUtil;

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
			boolean isArray = lambda.getInstantiatedMethodType().startsWith("([");
			if (isArray) {
				try {
					this.instantiatedTypes = new Type[]{Class.forName(lambda.getInstantiatedMethodType().replace("/", ".").substring(0, index).substring(1) + ";")};
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			} else {
				String[] instantiatedTypeNames = lambda.getInstantiatedMethodType().substring(2, index).split(";L");
				this.instantiatedTypes = new Type[instantiatedTypeNames.length];
				for (int i = 0; i < instantiatedTypeNames.length; i++) {
					this.instantiatedTypes[i] = ClassLoaderUtil.loadClass(instantiatedTypeNames[i]);
				}
			}
		} else {
			this.instantiatedTypes = new Type[0];
		}
		this.clazz = (Class<?>) FieldUtil.getFieldValue(executable, "clazz");
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
