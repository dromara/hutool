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

package org.dromara.hutool.core.bean.copier.provider;

import org.dromara.hutool.core.bean.DynaBean;
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.convert.Convert;

import java.lang.reflect.Type;

/**
 * DynaBean值提供者
 *
 * @author looly
 * @since 5.4.2
 */
public class DynaBeanValueProvider implements ValueProvider<String> {

	private final DynaBean dynaBean;
	private final boolean ignoreError;

	/**
	 * 构造
	 *
	 * @param dynaBean        DynaBean
	 * @param ignoreError 是否忽略错误
	 */
	public DynaBeanValueProvider(final DynaBean dynaBean, final boolean ignoreError) {
		this.dynaBean = dynaBean;
		this.ignoreError = ignoreError;
	}

	@Override
	public Object value(final String key, final Type valueType) {
		final Object value = dynaBean.get(key);
		return Convert.convertWithCheck(valueType, value, null, this.ignoreError);
	}

	@Override
	public boolean containsKey(final String key) {
		return dynaBean.containsProp(key);
	}

}
