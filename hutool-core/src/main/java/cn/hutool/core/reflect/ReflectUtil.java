package cn.hutool.core.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author Looly
 * @since 3.0.9
 */
public class ReflectUtil {

	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @since 4.6.8
	 */
	public static <T extends AccessibleObject> T setAccessible(final T accessibleObject) {
		if (null != accessibleObject && false == accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}

	/**
	 * 获取jvm定义的Field Descriptors（字段描述）
	 *
	 * @param executable 可执行的反射对象
	 * @return 描述符
	 * @apiNote 参考：<a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("hashCode"))                                                                 // "()I"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("toString"))                                                                // "()Ljava/lang/String;"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class))                                                         // "(Ljava/lang/Object;)Z"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ReflectUtil.class.getDeclaredMethod("appendDescriptor", Class.clas, StringBuilder.class))     // "(Ljava/lang/Class;Ljava/lang/StringBuilder;)V"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ArrayUtil.class.getMethod("isEmpty", Object[].class))                                         // "([Ljava/lang/Object;)Z"}</li>
	 * </ul>
	 *
	 * Object.class.getMethod("hashCode")
	 */
	public static String getDescriptor(Executable executable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('(');
		Class<?>[] parameters = executable.getParameterTypes();
		for (Class<?> parameter : parameters) {
			appendDescriptor(parameter, stringBuilder);
		}
		if (executable instanceof Method) {
			Method method = (Method) executable;
			stringBuilder.append(')');
			appendDescriptor(method.getReturnType(), stringBuilder);
			return stringBuilder.toString();
		} else if (executable instanceof Constructor) {
			return stringBuilder.append(")V").toString();
		}
		throw new IllegalArgumentException("Unknown Executable: " + executable);
	}

	/**
	 * 拼接描述符
	 *
	 * @param clazz         类
	 * @param stringBuilder 描述符
	 * @link <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 */
	private static void appendDescriptor(Class<?> clazz, StringBuilder stringBuilder) {
		Class<?> currentClass;
		for (currentClass = clazz;
			 currentClass.isArray();
			 currentClass = currentClass.getComponentType()) {
			stringBuilder.append('[');
		}
		if (currentClass.isPrimitive()) {
			stringBuilder.append(getDescriptorChar(currentClass));
		} else {
			stringBuilder.append('L').append(currentClass.getName().replace('.', '/')).append(';');
		}

	}

	/**
	 * 获取单个描述符
	 *
	 * @param currentClass 当前类
	 * @return 描述符
	 */
	private static char getDescriptorChar(Class<?> currentClass) {
		if (currentClass == Boolean.class || currentClass == boolean.class) {
			return 'Z';
		}
		if (currentClass == Byte.class || currentClass == byte.class) {
			return 'B';
		}
		if (currentClass == Short.class || currentClass == short.class) {
			return 'S';
		}
		if (currentClass == Character.class || currentClass == char.class) {
			return 'C';
		}
		if (currentClass == Integer.class || currentClass == int.class) {
			return 'I';
		}
		if (currentClass == Long.class || currentClass == long.class) {
			return 'J';
		}
		if (currentClass == Float.class || currentClass == float.class) {
			return 'F';
		}
		if (currentClass == Double.class || currentClass == double.class) {
			return 'D';
		}
		if (currentClass == Object.class) {
			return 'L';
		}
		if (currentClass == Void.class || currentClass == void.class) {
			return 'V';
		}
		throw new AssertionError();
	}

}
