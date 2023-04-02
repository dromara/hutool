/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.reflect;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 反射工具类
 *
 * <p>
 * 本工具类，v6.x进行了重构，原来{@link ReflectUtil}中的方法大部分被移动到了
 * {@link FieldUtil}、{@link MethodUtil}、{@link ModifierUtil}、{@link ConstructorUtil}等中，
 * 其他相关方法请参考<strong>org.dromara.hutool.core.reflect</strong>包下的类,相关类
 * </p>
 * <p>常用方法变更</p>
 * <ul>
 *     <li>反射修改属性</li>
 *     <li>{@code ReflectUtil#setFieldValue(Object, String, Object)} --p {@link FieldUtil#setFieldValue(Object, String, Object)}</li>
 *     <li>修改private修饰可被外部访问</li>
 *     <li>{@code ReflectUtil.setAccessible(ReflectUtil.getMethodByName(Xxx.class, "xxxMethodName"))} --p {@link ReflectUtil#setAccessible(AccessibleObject)} --p {@link MethodUtil#getMethodByName(Class, String)} </li>
 *     <li>移除final属性</li>
 *     <li>{@code ReflectUtil.removeFinalModify(Field)} --p {@link  ModifierUtil#removeFinalModify(Field)}</li>
 * </ul>
 *
 * @author Looly
 * @since 3.0.9
 */
public class ReflectUtil {

	/**
	 * 设置方法为可访问（私有方法可以被外部调用）<br>
	 * 注意此方法在jdk9+中抛出异常，须添加`--add-opens=java.base/java.lang=ALL-UNNAMED`启动参数
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @throws SecurityException 访问被禁止抛出此异常
	 * @since 4.6.8
	 */
	public static <T extends AccessibleObject> T setAccessible(final T accessibleObject) throws SecurityException {
		if (null != accessibleObject && false == accessibleObject.isAccessible()) {
			return AccessController.doPrivileged((PrivilegedAction<T>) () -> {
				// 特权访问
				accessibleObject.setAccessible(true);
				return accessibleObject;
			});
		}
		return accessibleObject;
	}

	/**
	 * 获取jvm定义的Field Descriptors（字段描述）
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("hashCode"))                                                                 // "()I"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("toString"))                                                                // "()Ljava/lang/String;"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Object.class.getMethod("equals", Object.class))                                                         // "(Ljava/lang/Object;)Z"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ReflectUtil.class.getDeclaredMethod("appendDescriptor", Class.clas, StringBuilder.class))     // "(Ljava/lang/Class;Ljava/lang/StringBuilder;)V"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(ArrayUtil.class.getMethod("isEmpty", Object[].class))                                         // "([Ljava/lang/Object;)Z"}</li>
	 * </ul>
	 *
	 * @param executable 可执行的反射对象
	 * @return 描述符
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 */
	public static String getDescriptor(final Executable executable) {
		final StringBuilder stringBuilder = new StringBuilder(32);
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
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code ReflectUtil.getDescriptor(boolean.class)                        "Z"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Boolean.class)                        "Ljava/lang/Boolean;"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(double[][][].class)                   "[[[D"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(int.class)                            "I"}</li>
	 *     <li>{@code ReflectUtil.getDescriptor(Integer.class)                        "Ljava/lang/Integer;"}</li>
	 * </ul>
	 *
	 * @param clazz 类
	 * @return 描述字符串
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 */
	public static String getDescriptor(final Class<?> clazz) {
		final StringBuilder stringBuilder = new StringBuilder(32);
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
