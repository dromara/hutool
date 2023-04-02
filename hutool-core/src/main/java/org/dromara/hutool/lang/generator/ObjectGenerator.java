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

package org.dromara.hutool.lang.generator;

import org.dromara.hutool.reflect.ConstructorUtil;

/**
 * 对象生成器，通过指定对象的Class类型，调用next方法时生成新的对象。
 *
 * @param <T> 对象类型
 * @author looly
 * @since 5.4.3
 */
public class ObjectGenerator<T> implements Generator<T> {

	private final Class<T> clazz;

	/**
	 * 构造
	 * @param clazz 对象类型
	 */
	public ObjectGenerator(final Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T next() {
		return ConstructorUtil.newInstanceIfPossible(this.clazz);
	}
}
