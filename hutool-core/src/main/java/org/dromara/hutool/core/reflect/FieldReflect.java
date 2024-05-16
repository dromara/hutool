/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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
