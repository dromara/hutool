/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.lang.Assert;

/**
 * 带根的转换器<br>
 * 在嵌套对象转换中，如果涉及子对象的转换，使用根转换器转换
 *
 * @author Looly
 * @since 6.0.0
 */
public abstract class ConverterWithRoot implements Converter {

	protected final Converter rootConverter;

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器
	 */
	public ConverterWithRoot(final Converter rootConverter) {
		this.rootConverter = Assert.notNull(rootConverter);
	}

	/**
	 * 获取根转换器，用于子转换器转换
	 *
	 * @return 根转换器
	 */
	public Converter getRootConverter() {
		return rootConverter;
	}
}
