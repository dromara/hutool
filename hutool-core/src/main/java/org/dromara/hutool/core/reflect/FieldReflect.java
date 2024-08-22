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
import org.dromara.hutool.core.lang.Assert;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * 字段反射类<br>
 * 此类持有类中字段的缓存，如果字段在类中修改，则需要手动调用clearCaches方法清除缓存。
 *
 * @author Looly
 * @since 6.0.0
 */
public class FieldReflect {

	/**
	 * 创建FieldReflect
	 *
	 * @param clazz 类
	 * @return FieldReflect
	 */
	public static FieldReflect of(final Class<?> clazz) {
		return new FieldReflect(clazz);
	}

	private final Class<?> clazz;
	private volatile Field[] declaredFields;
	private volatile Field[] allFields;

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public FieldReflect(final Class<?> clazz) {
		this.clazz = Assert.notNull(clazz);
	}

	/**
	 * 获取当前类
	 *
	 * @return 当前类
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * 清空缓存
	 */
	synchronized public void clearCaches() {
		declaredFields = null;
		allFields = null;
	}

	/**
	 * 获得当前类声明的所有字段（包括非public字段），但不包括父类的字段
	 *
	 * @param predicate 过滤器
	 * @return 字段数组
	 * @throws SecurityException 安全检查异常
	 */
	public Field[] getDeclaredFields(final Predicate<Field> predicate) {
		if (null == declaredFields) {
			synchronized (FieldReflect.class) {
				if (null == declaredFields) {
					declaredFields = clazz.getDeclaredFields();
				}
			}
		}
		return ArrayUtil.filter(declaredFields, predicate);
	}

	/**
	 * 获得当前类和父类声明的所有字段（包括非public字段）
	 *
	 * @param predicate 过滤器
	 * @return 字段数组
	 * @throws SecurityException 安全检查异常
	 */
	public Field[] getAllFields(final Predicate<Field> predicate) {
		if (null == allFields) {
			synchronized (FieldReflect.class) {
				if (null == allFields) {
					allFields = getFieldsDirectly(true);
				}
			}
		}
		return ArrayUtil.filter(allFields, predicate);
	}

	/**
	 * 获得一个类中所有字段列表，直接反射获取，无缓存<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param withSuperClassFields 是否包括父类的字段列表
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public Field[] getFieldsDirectly(final boolean withSuperClassFields) throws SecurityException {
		Field[] allFields = null;
		Class<?> searchType = this.clazz;
		Field[] declaredFields;
		while (searchType != null) {
			declaredFields = searchType.getDeclaredFields();
			if (null == allFields) {
				allFields = declaredFields;
			} else {
				allFields = ArrayUtil.append(allFields, declaredFields);
			}
			searchType = withSuperClassFields ? searchType.getSuperclass() : null;
		}

		return allFields;
	}
}
