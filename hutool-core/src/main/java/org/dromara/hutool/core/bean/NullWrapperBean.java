/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
