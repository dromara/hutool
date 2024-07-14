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
