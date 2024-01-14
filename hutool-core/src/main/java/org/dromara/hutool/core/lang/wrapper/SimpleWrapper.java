/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.wrapper;

/**
 * 简单包装对象<br>
 * 通过继承此类，可以直接使用被包装的对象，用于简化和统一封装。
 *
 * @param <T> 被包装对象类型
 * @author looly
 * @since 6.0.0
 */
public class SimpleWrapper<T> implements Wrapper<T> {

	protected final T raw;

	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public SimpleWrapper(final T raw) {
		this.raw = raw;
	}

	@Override
	public T getRaw() {
		return this.raw;
	}
}
