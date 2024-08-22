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

package org.dromara.hutool.core.bean.copier.provider;

import org.dromara.hutool.core.bean.BeanDesc;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.convert.Convert;

import java.lang.reflect.Type;

/**
 * Bean值提供器
 *
 * @author looly
 */
public class BeanValueProvider implements ValueProvider<String> {

	private final Object bean;
	private final BeanDesc beanDesc;

	/**
	 * 构造
	 *
	 * @param bean Bean
	 */
	public BeanValueProvider(final Object bean) {
		this(bean, null);
	}

	/**
	 * 构造
	 *
	 * @param bean Bean
	 * @param beanDesc 自定义的{@link BeanDesc}，默认为{@link BeanUtil#getBeanDesc(Class)}
	 */
	public BeanValueProvider(final Object bean, BeanDesc beanDesc) {
		this.bean = bean;
		if(null == beanDesc){
			beanDesc = BeanUtil.getBeanDesc(bean.getClass());
		}
		this.beanDesc = beanDesc;
	}

	@Override
	public Object value(final String key, final Type valueType) {
		final PropDesc prop = beanDesc.getProp(key);
		if (null != prop) {
			return Convert.convert(valueType, prop.getValue(bean));
		}
		return null;
	}

	@Override
	public boolean containsKey(final String key) {
		return null != beanDesc.getProp(key);
	}
}
