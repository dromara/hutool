/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	// region ----- hasAny

	/**
	 * 类是否存在给定修饰符中的<b>任意一个</b><br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的modifierTypes为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code true}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param clazz         类，如果为{@code null}返回{@code false}
	 * @param modifierTypes 修饰符枚举，如果为空返回{@code false}
	 * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAny(final Class<?> clazz, final ModifierType... modifierTypes) {
		if (null == clazz || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return hasAny(ClassMember.of(clazz), modifierTypes);
	}

	/**
	 * 成员是否存在给定修饰符中的<b>任意一个</b><br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的modifierTypes为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code true}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param member        构造、字段或方法，如果为{@code null}返回{@code false}
	 * @param modifierTypes 修饰符枚举，如果为空返回{@code false}
	 * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAny(final Member member, final ModifierType... modifierTypes) {
		if (null == member || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (member.getModifiers() & ModifierType.orToInt(modifierTypes));
	}

	/**
	 * 需要检查的修饰符中是否存在给定修饰符中的<b>任意一个</b><br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的modifierTypes为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code true}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param modifiers        类、构造、字段或方法的修饰符
	 * @param checkedModifiers 需要检查的修饰符，如果为空返回{@code false}
	 * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAny(final int modifiers, final int... checkedModifiers) {
		if (ArrayUtil.isEmpty(checkedModifiers)) {
			return false;
		}
		return 0 != (modifiers & ModifierType.orToInt(checkedModifiers));
	}
	// endregion

	// region ----- hasAll

	/**
	 * 类中是否同时存在<b>所有</b>给定修饰符<br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的modifierTypes为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code false}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param clazz         类，如果为{@code null}返回{@code false}
	 * @param modifierTypes 修饰符枚举，如果为空返回{@code false}
	 * @return 是否同时存在所有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAll(final Class<?> clazz, final ModifierType... modifierTypes) {
		if (null == clazz || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return hasAll(ClassMember.of(clazz), modifierTypes);
	}

	/**
	 * 成员中是否同时存在<b>所有</b>给定修饰符<br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的modifierTypes为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code false}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param member        构造、字段或方法，如果为{@code null}返回{@code false}
	 * @param modifierTypes 修饰符枚举，如果为空返回{@code false}
	 * @return 是否同时存在所有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAll(final Member member, final ModifierType... modifierTypes) {
		if (null == member || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		final int checkedModifiersInt = ModifierType.orToInt(modifierTypes);
		return checkedModifiersInt == (member.getModifiers() & checkedModifiersInt);
	}

	/**
	 * 需要检查的修饰符中是否同时存在<b>所有</b>给定修饰符<br>
	 * 如定义修饰符为：{@code public static final}，那么如果传入的checkedModifiers为：
	 * <ul>
	 *     <li>public、static  返回{@code true}</li>
	 *     <li>public、abstract返回{@code false}</li>
	 *     <li>private、abstract返回{@code false}</li>
	 * </ul>
	 *
	 * @param modifiers        类、构造、字段或方法的修饰符
	 * @param checkedModifiers 需要检查的修饰符，如果为空返回{@code false}
	 * @return 是否同时存在所有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
	 */
	public static boolean hasAll(final int modifiers, final int... checkedModifiers) {
		if (ArrayUtil.isEmpty(checkedModifiers)) {
			return false;
		}
		final int checkedModifiersInt = ModifierType.orToInt(checkedModifiers);
		return checkedModifiersInt == (modifiers & checkedModifiersInt);
	}
	// endregion

	// region ----- isXXX

	/**
	 * 提供的方法是否为default方法
	 *
	 * @param method 方法，如果为{@code null}返回{@code false}
	 * @return 是否为default方法
	 * @since 6.0.0
	 */
	public static boolean isDefault(final Method method) {
		return null != method && method.isDefault();
	}

	/**
	 * 是否是public成员，可检测包括构造、字段和方法
	 *
	 * @param member 构造、字段或方法，如果为{@code null}返回{@code false}
	 * @return 是否是public
	 */
	public static boolean isPublic(final Member member) {
		return null != member && Modifier.isPublic(member.getModifiers());
	}

	/**
	 * 是否是public类
	 *
	 * @param clazz 类，如果为{@code null}返回{@code false}
	 * @return 是否是public
	 */
	public static boolean isPublic(final Class<?> clazz) {
		return null != clazz && Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 是否是private成员，可检测包括构造、字段和方法
	 *
	 * @param member 构造、字段或方法，如果为{@code null}返回{@code false}
	 * @return 是否是private
	 */
	public static boolean isPrivate(final Member member) {
		return null != member && Modifier.isPrivate(member.getModifiers());
	}

	/**
	 * 是否是private类
	 *
	 * @param clazz 类，如果为{@code null}返回{@code false}
	 * @return 是否是private类
	 */
	public static boolean isPrivate(final Class<?> clazz) {
		return null != clazz && Modifier.isPrivate(clazz.getModifiers());
	}

	/**
	 * 是否是static成员，包括构造、字段或方法
	 *
	 * @param member 构造、字段或方法，如果为{@code null}返回{@code false}
	 * @return 是否是static
	 * @since 4.0.8
	 */
	public static boolean isStatic(final Member member) {
		return null != member && Modifier.isStatic(member.getModifiers());
	}

	/**
	 * 是否是static类
	 *
	 * @param clazz 类，如果为{@code null}返回{@code false}
	 * @return 是否是static
	 * @since 4.0.8
	 */
	public static boolean isStatic(final Class<?> clazz) {
		return null != clazz && Modifier.isStatic(clazz.getModifiers());
	}

	/**
	 * 是否是合成成员（由java编译器生成的）
	 *
	 * @param member 构造、字段或方法
	 * @return 是否是合成字段
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(final Member member) {
		return null != member && member.isSynthetic();
	}

	/**
	 * 是否是合成类（由java编译器生成的）
	 *
	 * @param clazz 类
	 * @return 是否是合成
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(final Class<?> clazz) {
		return null != clazz && clazz.isSynthetic();
	}

	/**
	 * 是否抽象成员
	 *
	 * @param member 构造、字段或方法
	 * @return 是否抽象方法
	 * @since 5.7.23
	 */
	public static boolean isAbstract(final Member member) {
		return null != member && Modifier.isAbstract(member.getModifiers());
	}

	/**
	 * 是否抽象类
	 *
	 * @param clazz 构造、字段或方法
	 * @return 是否抽象类
	 * @since 5.7.23
	 */
	public static boolean isAbstract(final Class<?> clazz) {
		return null != clazz && Modifier.isAbstract(clazz.getModifiers());
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
	// endregion

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
		if (!hasAny(field, ModifierType.FINAL)) {
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
}
