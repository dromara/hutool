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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.exception.HutoolException;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 修饰符工具类
 *
 * @author looly
 * @since 4.0.5
 */
public class ModifierUtil {

	/**
	 * 修饰符枚举
	 *
	 * @author looly
	 * @since 4.0.5
	 */
	public enum ModifierType {
		/**
		 * public修饰符，所有类都能访问
		 */
		PUBLIC(Modifier.PUBLIC),
		/**
		 * private修饰符，只能被自己访问和修改
		 */
		PRIVATE(Modifier.PRIVATE),
		/**
		 * protected修饰符，自身、子类及同一个包中类可以访问
		 */
		PROTECTED(Modifier.PROTECTED),
		/**
		 * static修饰符，（静态修饰符）指定变量被所有对象共享，即所有实例都可以使用该变量。变量属于这个类
		 */
		STATIC(Modifier.STATIC),
		/**
		 * final修饰符，最终修饰符，指定此变量的值不能变，使用在方法上表示不能被重载
		 */
		FINAL(Modifier.FINAL),
		/**
		 * synchronized，同步修饰符，在多个线程中，该修饰符用于在运行前，对他所属的方法加锁，以防止其他线程的访问，运行结束后解锁。
		 */
		SYNCHRONIZED(Modifier.SYNCHRONIZED),
		/**
		 * （易失修饰符）指定该变量可以同时被几个线程控制和修改
		 */
		VOLATILE(Modifier.VOLATILE),
		/**
		 * （过度修饰符）指定该变量是系统保留，暂无特别作用的临时性变量，序列化时忽略
		 */
		TRANSIENT(Modifier.TRANSIENT),
		/**
		 * native，本地修饰符。指定此方法的方法体是用其他语言在程序外部编写的。
		 */
		NATIVE(Modifier.NATIVE),

		/**
		 * abstract，将一个类声明为抽象类，没有实现的方法，需要子类提供方法实现。
		 */
		ABSTRACT(Modifier.ABSTRACT),
		/**
		 * strictfp，一旦使用了关键字strictfp来声明某个类、接口或者方法时，那么在这个关键字所声明的范围内所有浮点运算都是精确的，符合IEEE-754规范的。
		 */
		STRICT(Modifier.STRICT);

		/**
		 * 修饰符枚举对应的int修饰符值
		 */
		private final int value;

		/**
		 * 构造
		 *
		 * @param modifier 修饰符int表示，见{@link Modifier}
		 */
		ModifierType(final int modifier) {
			this.value = modifier;
		}

		/**
		 * 获取修饰符枚举对应的int修饰符值，值见{@link Modifier}
		 *
		 * @return 修饰符枚举对应的int修饰符值
		 */
		public int getValue() {
			return this.value;
		}
	}

	/**
	 * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
	 *
	 * @param clazz         类
	 * @param modifierTypes 修饰符枚举
	 * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasModifier(final Class<?> clazz, final ModifierType... modifierTypes) {
		if (null == clazz || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (clazz.getModifiers() & modifiersToInt(modifierTypes));
	}

	/**
	 * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
	 *
	 * @param member        构造、字段或方法
	 * @param modifierTypes 修饰符枚举
	 * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasModifier(final Member member, final ModifierType... modifierTypes) {
		if (null == member || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (member.getModifiers() & modifiersToInt(modifierTypes));
	}

	/**
	 * 提供的方法是否为default方法
	 *
	 * @param method 方法
	 * @return 是否为default方法
	 * @since 6.0.0
	 */
	public static boolean isDefault(final Method method) {
		return method.isDefault();
	}

	/**
	 * 是否是public成员，可检测包括构造、字段和方法
	 *
	 * @param member 构造、字段或方法
	 * @return 是否是public
	 */
	public static boolean isPublic(final Member member) {
		return Modifier.isPublic(member.getModifiers());
	}

	/**
	 * 是否是public类
	 *
	 * @param clazz 类
	 * @return 是否是public
	 */
	public static boolean isPublic(final Class<?> clazz) {
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 是否是private成员，可检测包括构造、字段和方法
	 *
	 * @param member 构造、字段或方法
	 * @return 是否是private
	 */
	public static boolean isPrivate(final Member member) {
		return Modifier.isPrivate(member.getModifiers());
	}

	/**
	 * 是否是private类
	 *
	 * @param clazz 类
	 * @return 是否是private类
	 */
	public static boolean isPrivate(final Class<?> clazz) {
		return Modifier.isPrivate(clazz.getModifiers());
	}

	/**
	 * 是否是static成员，包括构造、字段或方法
	 *
	 * @param member 构造、字段或方法
	 * @return 是否是static
	 * @since 4.0.8
	 */
	public static boolean isStatic(final Member member) {
		return Modifier.isStatic(member.getModifiers());
	}

	/**
	 * 是否是static类
	 *
	 * @param clazz 类
	 * @return 是否是static
	 * @since 4.0.8
	 */
	public static boolean isStatic(final Class<?> clazz) {
		return Modifier.isStatic(clazz.getModifiers());
	}

	/**
	 * 是否是合成成员（由java编译器生成的）
	 *
	 * @param member 构造、字段或方法
	 * @return 是否是合成字段
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(final Member member) {
		return member.isSynthetic();
	}

	/**
	 * 是否是合成类（由java编译器生成的）
	 *
	 * @param clazz 类
	 * @return 是否是合成
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(final Class<?> clazz) {
		return clazz.isSynthetic();
	}

	/**
	 * 是否抽象成员
	 *
	 * @param member 构造、字段或方法
	 * @return 是否抽象方法
	 * @since 5.7.23
	 */
	public static boolean isAbstract(final Member member) {
		return Modifier.isAbstract(member.getModifiers());
	}

	/**
	 * 是否抽象类
	 *
	 * @param clazz 构造、字段或方法
	 * @return 是否抽象类
	 * @since 5.7.23
	 */
	public static boolean isAbstract(final Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否抽象类
	 *
	 * @param clazz 构造、字段或方法
	 * @return 是否抽象类
	 */
	public static boolean isInterface(final Class<?> clazz) {
		return null != clazz && clazz.isInterface();
	}

	/**
	 * 设置final的field字段可以被修改
	 * 只要不会被编译器内联优化的 final 属性就可以通过反射有效的进行修改 --  修改后代码中可使用到新的值;
	 * <p>以下属性，编译器会内联优化，无法通过反射修改：</p>
	 * <ul>
	 *     <li> 基本类型 byte, char, short, int, long, float, double, boolean</li>
	 *     <li> Literal String 类型(直接双引号字符串)</li>
	 * </ul>
	 * <p>以下属性，可以通过反射修改：</p>
	 * <ul>
	 *     <li>基本类型的包装类 Byte、Character、Short、Long、Float、Double、Boolean</li>
	 *     <li>字符串，通过 new String("")实例化</li>
	 *     <li>自定义java类</li>
	 * </ul>
	 * <pre class="code">
	 * {@code
	 *      //示例，移除final修饰符
	 *      class JdbcDialects {private static final List<Number> dialects = new ArrayList<>();}
	 *      Field field = ReflectUtil.getField(JdbcDialects.class, fieldName);
	 * 		ReflectUtil.removeFinalModify(field);
	 * 		ReflectUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);
	 *    }
	 * </pre>
	 *
	 * <p>JDK9+此方法抛出NoSuchFieldException异常，原因是除非开放，否则模块外无法访问属性</p>
	 *
	 * @param field 被修改的field，不可以为空
	 * @throws HutoolException IllegalAccessException等异常包装
	 * @author dazer
	 * @since 5.8.8
	 */
	public static void removeFinalModify(final Field field) {
		if (!hasModifier(field, ModifierType.FINAL)) {
			return;
		}

		//将字段的访问权限设为true：即去除private修饰符的影响
		ReflectUtil.setAccessible(field);

		//去除final修饰符的影响，将字段设为可修改的
		final Field modifiersField;
		try {
			modifiersField = Field.class.getDeclaredField("modifiers");
		} catch (final NoSuchFieldException e) {
			throw new HutoolException(e, "Field [modifiers] not exist!");
		}

		try {
			//Field 的 modifiers 是私有的
			modifiersField.setAccessible(true);
			//& ：位与运算符，按位与；  运算规则：两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
			//~ ：位非运算符，按位取反；运算规则：转成二进制，如果位为0，结果是1，如果位为1，结果是0.
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		} catch (final IllegalAccessException e) {
			//内部，工具类，基本不抛出异常
			throw new HutoolException(e, "IllegalAccess for [{}.{}]", field.getDeclaringClass(), field.getName());
		}
	}
	//-------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 多个修饰符做“与”操作，表示同时存在多个修饰符
	 *
	 * @param modifierTypes 修饰符列表，元素不能为空
	 * @return “与”之后的修饰符
	 */
	private static int modifiersToInt(final ModifierType... modifierTypes) {
		int modifier = modifierTypes[0].getValue();
		for (int i = 1; i < modifierTypes.length; i++) {
			modifier |= modifierTypes[i].getValue();
		}
		return modifier;
	}
	//-------------------------------------------------------------------------------------------------------- Private method end
}
