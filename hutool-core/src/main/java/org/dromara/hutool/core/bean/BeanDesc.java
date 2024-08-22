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

package org.dromara.hutool.core.bean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Bean描述，通过反射等方式获取Bean的setter、getter、字段等信息
 *
 * @author Looly
 * @since 6.0.0
 */
public interface BeanDesc extends Serializable {

	/**
	 * 获取字段名-字段属性Map
	 *
	 * @param ignoreCase 是否忽略大小写，true为忽略，false不忽略
	 * @return 字段名-字段属性Map
	 */
	Map<String, PropDesc> getPropMap(final boolean ignoreCase);

	/**
	 * 获取字段属性列表
	 *
	 * @return {@link PropDesc} 列表
	 */
	default Collection<PropDesc> getProps() {
		return getPropMap(false).values();
	}

	/**
	 * 获取属性，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return {@link PropDesc}
	 */
	default PropDesc getProp(final String fieldName) {
		return getPropMap(false).get(fieldName);
	}

	/**
	 * 获取Getter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Getter方法
	 */
	default Method getGetter(final String fieldName) {
		final PropDesc desc = getProp(fieldName);
		return null == desc ? null : desc.getGetter();
	}

	/**
	 * 获取Setter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Setter方法
	 */
	default Method getSetter(final String fieldName) {
		final PropDesc desc = getProp(fieldName);
		return null == desc ? null : desc.getSetter();
	}
}
