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

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.FieldUtil;

import java.lang.reflect.Field;

/**
 * Bean字段排序器<br>
 * 参阅feilong-core中的PropertyComparator
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class FieldsComparator<T> extends NullComparator<T> {
	private static final long serialVersionUID = 8649196282886500803L;

	/**
	 * 构造
	 *
	 * @param beanClass  Bean类
	 * @param fieldNames 多个字段名
	 */
	public FieldsComparator(final Class<T> beanClass, final String... fieldNames) {
		this(true, beanClass, fieldNames);
	}

	/**
	 * 构造
	 *
	 * @param nullGreater 是否{@code null}在后
	 * @param beanClass   Bean类
	 * @param fieldNames  多个字段名
	 */
	public FieldsComparator(final boolean nullGreater, final Class<T> beanClass, final String... fieldNames) {
		super(nullGreater, (a, b) -> {
			Field field;
			for (final String fieldName : fieldNames) {
				field = FieldUtil.getField(beanClass, fieldName);
				Assert.notNull(field, "Field [{}] not found in Class [{}]", fieldName, beanClass.getName());
				final int compare = new FieldComparator<>(field).compare(a, b);
				if (0 != compare) {
					return compare;
				}
			}
			return 0;
		});
	}

}
