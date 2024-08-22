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

/**
 * 为了解决反射过程中,需要传递null参数,但是会丢失参数类型而设立的包装类
 *
 * @param <T> Null值对应的类型
 * @author Lillls
 * @since 5.5.0
 */
public class NullWrapperBean<T> {

	private final Class<T> clazz;

	/**
	 * @param clazz null的类型
	 */
	public NullWrapperBean(final Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 获取null值对应的类型
	 *
	 * @return 类型
	 */
	public Class<T> getWrappedClass() {
		return clazz;
	}
}
