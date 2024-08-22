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

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;

/**
 * 类转换器<br>
 * 将类名转换为类，默认初始化这个类（执行static块）
 *
 * @author Looly
 */
public class ClassConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static ClassConverter INSTANCE = new ClassConverter();

	private final boolean isInitialized;

	/**
	 * 构造
	 */
	public ClassConverter() {
		this(true);
	}

	/**
	 * 构造
	 *
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @since 5.5.0
	 */
	public ClassConverter(final boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Override
	protected Class<?> convertInternal(final Class<?> targetClass, final Object value) {
		return ClassLoaderUtil.loadClass(convertToStr(value), isInitialized);
	}

}
