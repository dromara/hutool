/*
 * Copyright (c) 2013-2024 Hutool Team.
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
				final int compare = new FieldComparator<>(true, false, field).compare(a, b);
				if (0 != compare) {
					return compare;
				}
			}
			return 0;
		});
	}

}
