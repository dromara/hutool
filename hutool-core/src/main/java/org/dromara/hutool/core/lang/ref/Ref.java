/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.ref;

/**
 * 针对{@link java.lang.ref.Reference}的接口定义，用于扩展功能<br>
 * 例如提供自定义的无需回收对象
 *
 * @param <T> 对象类型
 */
@FunctionalInterface
public interface Ref<T> {

	/**
	 * 获取引用的原始对象
	 *
	 * @return 原始对象
	 */
	T get();
}
