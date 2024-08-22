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

import org.dromara.hutool.core.bean.path.AbstractBeanDesc;
import org.dromara.hutool.core.reflect.method.MethodNameUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.util.BooleanUtil;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 简单的Bean描述，只查找getter和setter方法，规则如下：
 * <ul>
 *     <li>不匹配字段，只查找getXXX、isXXX、setXXX方法。</li>
 *     <li>如果同时存在getXXX和isXXX，返回值为Boolean或boolean，isXXX优先。</li>
 *     <li>如果同时存在setXXX的多个重载方法，最小子类优先，如setXXX(List)优先于setXXX(Collection)</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public class SimpleBeanDesc extends AbstractBeanDesc {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public SimpleBeanDesc(final Class<?> beanClass) {
		super(beanClass);
		init();
	}

	/**
	 * 普通Bean初始化，查找和加载getter和setter<br>
	 */
	private void init() {
		final Map<String, PropDesc> propMap = this.propMap;

		final Method[] gettersAndSetters = MethodUtil.getPublicMethods(this.beanClass, MethodUtil::isGetterOrSetterIgnoreCase);
		boolean isSetter;
		int nameIndex;
		String methodName;
		String fieldName;
		for (final Method method : gettersAndSetters) {
			methodName = method.getName();
			switch (methodName.charAt(0)){
				case 's':
					isSetter = true;
					nameIndex = 3;
					break;
				case 'g':
					isSetter = false;
					nameIndex = 3;
					break;
				case 'i':
					isSetter = false;
					nameIndex = 2;
					break;
				default:
					continue;
			}

			fieldName = MethodNameUtil.decapitalize(methodName.substring(nameIndex));
			PropDesc propDesc = propMap.get(fieldName);
			if(null == propDesc){
				propDesc = new PropDesc(fieldName, isSetter ? null : method, isSetter ? method : null);
				propMap.put(fieldName, propDesc);
			} else{
				if(isSetter){
					if(null == propDesc.setter ||
						propDesc.setter.getParameterTypes()[0].isAssignableFrom(method.getParameterTypes()[0])){
						// 如果存在多个重载的setter方法，选择参数类型最匹配的
						propDesc.setter = method;
					}
				}else{
					if(null == propDesc.getter ||
						(BooleanUtil.isBoolean(propDesc.getter.getReturnType()) &&
							BooleanUtil.isBoolean(method.getReturnType()) &&
							methodName.startsWith(MethodNameUtil.IS_PREFIX))){
						// 如果返回值为Boolean或boolean，isXXX优先于getXXX
						propDesc.getter = method;
					}
				}
			}
		}
	}
}
