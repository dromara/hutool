/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.TripleTable;
import org.dromara.hutool.core.text.StrTrimer;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * 类描述工具类<br>
 * 来自：org.apache.dubbo.common.utils.ReflectUtils<br>
 * 在字节码中，类型表示如下：
 * <ul>
 *     <li>byte    =》 B</li>
 *     <li>char   =》  C</li>
 *     <li>double =》  D</li>
 *     <li>long   =》  J</li>
 *     <li>short  =》  S</li>
 *     <li>boolean =》 Z</li>
 *     <li>void    =》 V</li>
 *     <li>对象类型以“L”开头，“;”结尾，如Ljava/lang/Object;</li>
 *     <li>数组类型，每一位使用一个前置的[字符来描述，如：java.lang.String[][] =》 [[Ljava/lang/String;</li>
 * </ul>
 *
 * <p>此类旨在通过类描述信息和类名查找对应的类，如动态加载类等。</p>
 *
 * @author Dubbo
 * @since 6.0.0
 */
public class ClassDescUtil {

	// region ----- const
	/**
	 * void(V).
	 */
	public static final char JVM_VOID = 'V';

	/**
	 * boolean(Z).
	 */
	public static final char JVM_BOOLEAN = 'Z';

	/**
	 * byte(B).
	 */
	public static final char JVM_BYTE = 'B';

	/**
	 * char(C).
	 */
	public static final char JVM_CHAR = 'C';

	/**
	 * double(D).
	 */
	public static final char JVM_DOUBLE = 'D';

	/**
	 * float(F).
	 */
	public static final char JVM_FLOAT = 'F';

	/**
	 * int(I).
	 */
	public static final char JVM_INT = 'I';

	/**
	 * long(J).
	 */
	public static final char JVM_LONG = 'J';

	/**
	 * short(S).
	 */
	public static final char JVM_SHORT = 'S';
	// endregion

	/**
	 * 9种原始类型对应表<br>
	 * <pre>
	 *     左：原始类型
	 *     中：原始类型描述符
	 *     右：原始类型名称
	 * </pre>
	 */
	private static final TripleTable<Class<?>, Character, String> PRIMITIVE_TABLE = new TripleTable<>(9);

	static {
		PRIMITIVE_TABLE.put(void.class, JVM_VOID, "void");
		PRIMITIVE_TABLE.put(boolean.class, JVM_BOOLEAN, "boolean");
		PRIMITIVE_TABLE.put(byte.class, JVM_BYTE, "byte");
		PRIMITIVE_TABLE.put(char.class, JVM_CHAR, "char");
		PRIMITIVE_TABLE.put(double.class, JVM_DOUBLE, "double");
		PRIMITIVE_TABLE.put(float.class, JVM_FLOAT, "float");
		PRIMITIVE_TABLE.put(int.class, JVM_INT, "int");
		PRIMITIVE_TABLE.put(long.class, JVM_LONG, "long");
		PRIMITIVE_TABLE.put(short.class, JVM_SHORT, "short");
	}

	/**
	 * Class描述转Class
	 * <pre>{@code
	 * "[Z" => boolean[].class
	 * "[[Ljava/util/Map;" => java.util.Map[][].class
	 * }</pre>
	 *
	 * @param desc 类描述
	 * @return Class
	 * @throws HutoolException 类没有找到
	 */
	public static Class<?> descToClass(final String desc) throws HutoolException {
		return descToClass(desc, true, null);
	}

	/**
	 * Class描述转Class
	 * <pre>{@code
	 * "[Z" => boolean[].class
	 * "[[Ljava/util/Map;" => java.util.Map[][].class
	 * }</pre>
	 *
	 * @param desc          类描述
	 * @param isInitialized 是否初始化类
	 * @param cl            {@link ClassLoader}
	 * @return Class
	 * @throws HutoolException 类没有找到
	 */
	public static Class<?> descToClass(String desc, final boolean isInitialized, final ClassLoader cl) throws HutoolException {
		Assert.notNull(desc, "Name must not be null");
		final char firstChar = desc.charAt(0);
		final Class<?> clazz = PRIMITIVE_TABLE.getLeftByMiddle(firstChar);
		if (null != clazz) {
			return clazz;
		}

		// 去除尾部多余的"."和"/"
		desc = StrUtil.trim(desc, StrTrimer.TrimMode.SUFFIX, (c) ->
			CharUtil.SLASH == c || CharUtil.DOT == c);

		if ('L' == firstChar) {
			// 正常类的描述中需要去掉L;包装的修饰
			// "Ljava/lang/Object;" ==> "java.lang.Object"
			desc = desc.substring(1, desc.length() - 1);
		}

		return ClassUtil.forName(desc, isInitialized, cl);
	}

	/**
	 * 获取类描述，这是编译成class文件后的二进制名称
	 * <pre>{@code
	 *    getDesc(boolean.class)       // Z
	 *    getDesc(Boolean.class)       // Ljava/lang/Boolean;
	 *    getDesc(double[][][].class)  // [[[D
	 *    getDesc(int.class)           // I
	 *    getDesc(Integer.class)       // Ljava/lang/Integer;
	 * }</pre>
	 *
	 * @param c class.
	 * @return desc.
	 *
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 */
	public static String getDesc(Class<?> c) {
		final StringBuilder ret = new StringBuilder();

		while (c.isArray()) {
			ret.append('[');
			c = c.getComponentType();
		}

		if (c.isPrimitive()) {
			final Character desc = PRIMITIVE_TABLE.getMiddleByLeft(c);
			if (null != desc) {
				ret.append(desc.charValue());
			}
		} else {
			ret.append('L');
			ret.append(c.getName().replace('.', '/'));
			ret.append(';');
		}
		return ret.toString();
	}

	/**
	 * 获取方法或构造描述<br>
	 * 方法（appendName为{@code true}）：
	 * <pre>{@code
	 *    int do(int arg1) => "do(I)I"
	 *    void do(String arg1,boolean arg2) => "do(Ljava/lang/String;Z)V"
	 * }</pre>
	 * 构造：
	 * <pre>{@code
	 *    "()V", "(Ljava/lang/String;I)V"
	 * }</pre>
	 *
	 * <p>当appendName为{@code false}时：</p>
	 * <pre>{@code
	 *    getDesc(Object.class.getMethod("hashCode"))                    // ()I
	 *    getDesc(Object.class.getMethod("toString"))                    // ()Ljava/lang/String;
	 *    getDesc(Object.class.getMethod("equals", Object.class))        // (Ljava/lang/Object;)Z
	 *    getDesc(ArrayUtil.class.getMethod("isEmpty", Object[].class))  // "([Ljava/lang/Object;)Z"
	 * }</pre>
	 *
	 * @param methodOrConstructor 方法或构造
	 * @param appendName 是否包含方法名称
	 * @return 描述
	 *
	 * @author VampireAchao
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">jvm定义的Field Descriptors（字段描述）</a>
	 * @see <a href="https://vampireAchao.gitee.io/2022/06/07/%E7%B1%BB%E5%9E%8B%E6%8F%8F%E8%BF%B0%E7%AC%A6/">关于类型描述符的博客</a>
	 */
	public static String getDesc(final Executable methodOrConstructor, final boolean appendName) {
		final StringBuilder ret = new StringBuilder();
		if (appendName && methodOrConstructor instanceof Method) {
			ret.append(methodOrConstructor.getName());
		}
		ret.append('(');

		// 参数
		final Class<?>[] parameterTypes = methodOrConstructor.getParameterTypes();
		for (final Class<?> parameterType : parameterTypes) {
			ret.append(getDesc(parameterType));
		}

		// 返回类型或构造标记
		ret.append(')');
		if (methodOrConstructor instanceof Method) {
			ret.append(getDesc(((Method) methodOrConstructor).getReturnType()));
		} else {
			ret.append('V');
		}

		return ret.toString();
	}

	/**
	 * 获得类名称<br>
	 * 数组输出xxx[]形式，其它类调用{@link Class#getName()}
	 *
	 * <pre>{@code
	 * java.lang.Object[][].class => "java.lang.Object[][]"
	 * }</pre>
	 *
	 * @param c 类
	 * @return 类名称
	 */
	public static String getName(Class<?> c) {
		if (c.isArray()) {
			final StringBuilder sb = new StringBuilder();
			do {
				sb.append("[]");
				c = c.getComponentType();
			}
			while (c.isArray());
			return c.getName() + sb;
		}
		return c.getName();
	}

	/**
	 * 获取构造或方法的名称表示<br>
	 * 构造：
	 * <pre>
	 * "()", "(java.lang.String,int)"
	 * </pre>
	 * <p>
	 * 方法：
	 * <pre>
	 *     "void do(int)", "void do()", "int do(java.lang.String,boolean)"
	 * </pre>
	 *
	 * @param executable 方法或构造
	 * @return 名称
	 */
	public static String getName(final Executable executable) {
		final StringBuilder ret = new StringBuilder("(");

		if (executable instanceof Method) {
			ret.append(getName(((Method) executable).getReturnType())).append(CharUtil.SPACE);
		}

		// 参数
		final Class<?>[] parameterTypes = executable.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (i > 0) {
				ret.append(',');
			}
			ret.append(getName(parameterTypes[i]));
		}

		ret.append(')');
		return ret.toString();
	}

	/**
	 * 类名称转类
	 *
	 * <pre>{@code
	 * "boolean" => boolean.class
	 * "java.util.Map[][]" => java.util.Map[][].class
	 * }</pre>
	 *
	 * @param name          name.
	 * @param isInitialized 是否初始化类
	 * @param cl            ClassLoader instance.
	 * @return Class instance.
	 */
	public static Class<?> nameToClass(String name, final boolean isInitialized, final ClassLoader cl) {
		Assert.notNull(name, "Name must not be null");
		// 去除尾部多余的"."和"/"
		name = StrUtil.trim(name, StrTrimer.TrimMode.SUFFIX, (c) ->
			CharUtil.SLASH == c || CharUtil.DOT == c);

		int c = 0;
		final int index = name.indexOf('[');
		if (index > 0) {
			// c是[]对个数，如String[][]，则表示二维数组，c的值是2，获得desc结果就是[[LString;
			c = (name.length() - index) / 2;
			name = name.substring(0, index);
		}

		if (c > 0) {
			final StringBuilder sb = new StringBuilder();
			while (c-- > 0) {
				sb.append('[');
			}

			final Class<?> clazz = PRIMITIVE_TABLE.getLeftByRight(name);
			if (null != clazz) {
				// 原始类型数组，根据name获取其描述
				sb.append(PRIMITIVE_TABLE.getMiddleByLeft(clazz).charValue());
			} else {
				// 对象数组必须转换为desc形式
				// "java.lang.Object" ==> "Ljava.lang.Object;"
				sb.append('L').append(name).append(';');
			}
			name = sb.toString();
		} else {
			final Class<?> clazz = PRIMITIVE_TABLE.getLeftByRight(name);
			if (null != clazz) {
				return clazz;
			}
		}

		return ClassUtil.forName(name, isInitialized, cl);
	}

	/**
	 * 类名称转描述
	 *
	 * <pre>{@code
	 * java.util.Map[][] => "[[Ljava/util/Map;"
	 * }</pre>
	 *
	 * @param name 名称
	 * @return 描述
	 */
	public static String nameToDesc(String name) {
		final StringBuilder sb = new StringBuilder();
		int c = 0;
		final int index = name.indexOf('[');
		if (index > 0) {
			c = (name.length() - index) / 2;
			name = name.substring(0, index);
		}
		while (c-- > 0) {
			sb.append('[');
		}

		final Class<?> clazz = PRIMITIVE_TABLE.getLeftByRight(name);
		if (null != clazz) {
			// 原始类型数组，根据name获取其描述
			sb.append(PRIMITIVE_TABLE.getMiddleByLeft(clazz).charValue());
		} else {
			// "java.lang.Object" ==> "Ljava.lang.Object;"
			sb.append('L').append(name.replace(CharUtil.DOT, CharUtil.SLASH)).append(';');
		}

		return sb.toString();
	}

	/**
	 * 类描述转名称
	 *
	 * <pre>{@code
	 * "[[I" => "int[][]"
	 * }</pre>
	 *
	 * @param desc 描述
	 * @return 名称
	 */
	public static String descToName(final String desc) {
		final StringBuilder sb = new StringBuilder();
		int c = desc.lastIndexOf('[') + 1;
		if (desc.length() == c + 1) {
			final char descChar = desc.charAt(c);
			final Class<?> clazz = PRIMITIVE_TABLE.getLeftByMiddle(descChar);
			if (null != clazz) {
				sb.append(PRIMITIVE_TABLE.getRightByLeft(clazz));
			} else {
				throw new HutoolException("Unsupported primitive desc: {}", desc);
			}
		} else {
			sb.append(desc.substring(c + 1, desc.length() - 1).replace(CharUtil.SLASH, CharUtil.DOT));
		}
		while (c-- > 0) {
			sb.append("[]");
		}
		return sb.toString();
	}

	/**
	 * 获取code base
	 *
	 * @param clazz 类
	 * @return code base
	 */
	public static String getCodeBase(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final ProtectionDomain domain = clazz.getProtectionDomain();
		if (domain == null) {
			return null;
		}
		final CodeSource source = domain.getCodeSource();
		if (source == null) {
			return null;
		}
		final URL location = source.getLocation();
		if (location == null) {
			return null;
		}
		return location.getFile();
	}
}
