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

import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.lang.reflect.*;

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
	 * 设置方法为可访问（私有方法可以被外部调用），静默调用，抛出异常则跳过<br>
	 * 注意此方法在jdk9+中抛出异常，须添加`--add-opens=java.base/java.lang=ALL-UNNAMED`启动参数
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @throws SecurityException 访问被禁止抛出此异常
	 */
	public static <T extends AccessibleObject> T setAccessibleQuietly(final T accessibleObject) {
		try{
			setAccessible(accessibleObject);
		} catch (final RuntimeException ignore){
			// ignore
		}
		return accessibleObject;
	}

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
		if (null != accessibleObject && !accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}
}
