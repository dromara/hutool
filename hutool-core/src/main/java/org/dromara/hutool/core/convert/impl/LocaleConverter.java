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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Locale;

/**
 *
 * {@link Locale}对象转换器<br>
 * 只提供String转换支持
 *
 * @author Looly
 * @since 4.5.2
 */
public class LocaleConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected Locale convertInternal(final Class<?> targetClass, final Object value) {
		try {
			final String str = convertToStr(value);
			if (StrUtil.isEmpty(str)) {
				return null;
			}

			final String[] items = str.split("_");
			if (items.length == 1) {
				return new Locale(items[0]);
			}
			if (items.length == 2) {
				return new Locale(items[0], items[1]);
			}
			return new Locale(items[0], items[1], items[2]);
		} catch (final Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
