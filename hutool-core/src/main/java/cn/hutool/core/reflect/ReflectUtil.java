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
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("hashCode"))                                                                 // "()I"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("toString"))                                                                // "()Ljava/lang/String;"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class))                                                         // "(Ljava/lang/Object;)Z"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ReflectUtil.class.getDeclaredMethod("appendDescriptor", Class.clas, StringBuilder.class))     // "(Ljava/lang/Class;Ljava/lang/StringBuilder;)V"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ArrayUtil.class.getMethod("isEmpty", Object[].class))                                         // "([Ljava/lang/Object;)Z"}</li>
	 * </ul>
	 */
	public static String getDescriptor(final Executable executable) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('(');
		final Class<?>[] parameters = executable.getParameterTypes();
		for (final Class<?> parameter : parameters) {
			stringBuilder.append(getDescriptor(parameter));
		}
		if (executable instanceof Method) {
			final Method method = (Method) executable;
			return stringBuilder.append(')').append(getDescriptor(method.getReturnType())).toString();
		} else if (executable instanceof Constructor) {
			return stringBuilder.append(")V").toString();
		}
		throw new IllegalArgumentException("Unknown Executable: " + executable);
	}

	/**
	 * 获取类型描述符，这是编译成class文件后的二进制名称
	 *
	 * @param clazz 类
	 * @return 描述字符串
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code ReflectUtil.getDescriptor(boolean.class)                        "Z"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Boolean.class)                        "Ljava/lang/Boolean;"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(double[][][].class)                   "[[[D"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(int.class)                            "I"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Integer.class)                        "Ljava/lang/Integer;"}</li>
	 * </ul>
	 */
	public static String getDescriptor(final Class<?> clazz) {
		final StringBuilder stringBuilder = new StringBuilder();
		Class<?> currentClass;
		for (currentClass = clazz;
			 currentClass.isArray();
			 currentClass = currentClass.getComponentType()) {
			// 如果当前是数组描述符
			stringBuilder.append('[');
		}
		if (currentClass.isPrimitive()) {
			// 只有下面九种基础数据类型以及数组，才有独立的描述符
			final char descriptor;
			// see sun.invoke.util.Wrapper
			// These must be in the order defined for widening primitive conversions in JLS 5.1.2
			if (currentClass == boolean.class) {
				descriptor = 'Z';
			} else if (currentClass == byte.class) {
				descriptor = 'B';
			} else if (currentClass == short.class) {
				descriptor = 'S';
			} else if (currentClass == char.class) {
				descriptor = 'C';
			} else if (currentClass == int.class) {
				descriptor = 'I';
			} else if (currentClass == long.class) {
				descriptor = 'J';
			} else if (currentClass == float.class) {
				descriptor = 'F';
			} else if (currentClass == double.class) {
				descriptor = 'D';
			} else if (currentClass == void.class) {
				// VOID must be the last type, since it is "assignable" from any other type:
				descriptor = 'V';
			} else {
				throw new AssertionError();
			}
			stringBuilder.append(descriptor);
		} else {
			// 否则一律是 "L"+类名.replace('.', '/')+";"格式的对象类型
			stringBuilder.append('L').append(currentClass.getName().replace('.', '/')).append(';');
		}
		return stringBuilder.toString();
	}

}
