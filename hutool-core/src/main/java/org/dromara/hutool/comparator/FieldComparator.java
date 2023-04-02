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

package org.dromara.hutool.comparator;

import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.reflect.FieldUtil;
import org.dromara.hutool.text.StrUtil;

import java.lang.reflect.Field;

/**
 * Bean字段排序器<br>
 * 参阅feilong-core中的PropertyComparator
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class FieldComparator<T> extends FuncComparator<T> {
	private static final long serialVersionUID = 9157326766723846313L;

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 * @param fieldName 字段名
	 */
	public FieldComparator(final Class<T> beanClass, final String fieldName) {
		this(getNonNullField(beanClass, fieldName));
	}

	/**
	 * 构造
	 *
	 * @param field 字段
	 */
	public FieldComparator(final Field field) {
		this(true, field);
	}

	/**
	 * 构造
	 *
	 * @param nullGreater 是否{@code null}在后
	 * @param field       字段
	 */
	public FieldComparator(final boolean nullGreater, final Field field) {
		super(nullGreater, (bean) ->
				(Comparable<?>) FieldUtil.getFieldValue(bean,
						Assert.notNull(field, "Field must be not null!")));
	}

	/**
	 * 获取字段，附带检查字段不存在的问题。
	 *
	 * @param beanClass Bean类
	 * @param fieldName 字段名
	 * @return 非null字段
	 */
	private static Field getNonNullField(final Class<?> beanClass, final String fieldName) {
		final Field field = FieldUtil.getField(beanClass, fieldName);
		if (field == null) {
			throw new IllegalArgumentException(StrUtil.format("Field [{}] not found in Class [{}]", fieldName, beanClass.getName()));
		}
		return field;
	}
}
