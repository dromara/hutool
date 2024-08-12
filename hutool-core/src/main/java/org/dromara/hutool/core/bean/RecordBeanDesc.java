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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.path.AbstractBeanDesc;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 针对Reccord类的Bean描述<br>
 * Bean描述包括Record自定义字段及对应方法，getter方法与字段名同名，不支持setter
 *
 * @author looly
 * @since 3.1.2
 */
public class RecordBeanDesc extends AbstractBeanDesc {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public RecordBeanDesc(final Class<?> beanClass) {
		super(beanClass);
		initForRecord();
	}

	// ------------------------------------------------------------------------------------------------------ Private method start

	/**
	 * 针对Record类的反射初始化
	 */
	private void initForRecord() {
		final Class<?> beanClass = this.beanClass;
		final Map<String, PropDesc> propMap = this.propMap;

		final Method[] getters = MethodUtil.getPublicMethods(beanClass, method -> 0 == method.getParameterCount());
		// 排除静态属性和对象子类
		final Field[] fields = FieldUtil.getFields(beanClass, field -> !ModifierUtil.isStatic(field) && !FieldUtil.isOuterClassField(field));
		for (final Field field : fields) {
			for (final Method getter : getters) {
				if (field.getName().equals(getter.getName())) {
					//record对象，getter方法与字段同名
					final PropDesc prop = new PropDesc(field, getter, null);
					propMap.putIfAbsent(prop.getFieldName(), prop);
				}
			}
		}
	}
	// ------------------------------------------------------------------------------------------------------ Private method end
}
