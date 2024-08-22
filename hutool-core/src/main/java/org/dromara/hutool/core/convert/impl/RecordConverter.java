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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.RecordUtil;
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.copier.provider.BeanValueProvider;
import org.dromara.hutool.core.bean.copier.provider.MapValueProvider;
import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertException;

import java.util.Map;

/**
 * Record类的转换器，支持：
 * <pre>
 *   Map =》 Record
 *   Bean =》 Record
 *   ValueProvider =》 Record
 * </pre>
 */
public class RecordConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static RecordConverter INSTANCE = new RecordConverter();

	@SuppressWarnings("unchecked")
	@Override
	protected Object convertInternal(final Class<?> targetClass, final Object value) {
		ValueProvider<String> valueProvider = null;
		if (value instanceof ValueProvider) {
			valueProvider = (ValueProvider<String>) value;
		} else if (value instanceof Map) {
			valueProvider = new MapValueProvider((Map<String, ?>) value);
		} else if (BeanUtil.isReadableBean(value.getClass())) {
			valueProvider = new BeanValueProvider(value);
		}

		if (null != valueProvider) {
			return RecordUtil.newInstance(targetClass, valueProvider);
		}

		throw new ConvertException("Unsupported source type: [{}] to [{}]", value.getClass(), targetClass);
	}
}
