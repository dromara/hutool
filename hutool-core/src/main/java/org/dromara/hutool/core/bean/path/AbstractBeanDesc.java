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

package org.dromara.hutool.core.bean.path;

import org.dromara.hutool.core.bean.BeanDesc;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.CaseInsensitiveMap;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bean描述抽象类
 *
 * @author Looly
 * @since 6.0.0
 */
public abstract class AbstractBeanDesc implements BeanDesc {
	private static final long serialVersionUID = 1L;

	/**
	 * Bean类
	 */
	protected final Class<?> beanClass;

	/**
	 * 属性Map
	 */
	protected final Map<String, PropDesc> propMap = new LinkedHashMap<>();

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public AbstractBeanDesc(final Class<?> beanClass) {
		this.beanClass = Assert.notNull(beanClass);
	}

	/**
	 * 获取Bean的全类名
	 *
	 * @return Bean的类名
	 */
	public String getName() {
		return this.beanClass.getName();
	}

	/**
	 * 获取Bean的简单类名
	 *
	 * @return Bean的类名
	 */
	public String getSimpleName() {
		return this.beanClass.getSimpleName();
	}

	/**
	 * 获取Bean类
	 *
	 * @return Bean类
	 */
	public Class<?> getBeanClass() {
		return this.beanClass;
	}

	@Override
	public Map<String, PropDesc> getPropMap(final boolean ignoreCase) {
		return ignoreCase ? new CaseInsensitiveMap<>(1, this.propMap) : this.propMap;
	}

	/**
	 * 获得字段名对应的字段对象，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	public Field getField(final String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getField();
	}
}
