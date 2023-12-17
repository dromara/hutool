/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.pool;

/**
 * 对象工厂接口，用于自定义对象创建、验证和销毁<br>
 * 来自：https://github.com/DanielYWoo/fast-object-pool/
 *
 * @param <T> 对象类型
 * @author Daniel
 */
public interface ObjectFactory<T> {
	/**
	 * 创建对象
	 *
	 * @return 创建的对象
	 */
	T create();

	/**
	 * 验证对象可用性，一般用于对象池中借出对象和返还对象前的验证操作。
	 *
	 * @param t 被验证的对象
	 * @return 是否可用
	 */
	boolean validate(T t);

	/**
	 * 销毁对象，用于在验证对象不可用或不需要时的销毁逻辑。
	 *
	 * @param t 被销毁的对象
	 */
	void destroy(T t);
}
