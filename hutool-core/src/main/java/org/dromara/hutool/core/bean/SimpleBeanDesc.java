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

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 简单的Bean描述，只查找getter和setter方法
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
					propDesc.setter = method;
				}else{
					propDesc.getter = method;
				}
			}
		}
	}
}
