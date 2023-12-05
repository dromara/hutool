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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.core.lang.wrapper.Wrapper;

/**
 * {@code JSONString}接口定义了一个{@code toJSONString()}<br>
 * 实现此接口的类可以通过实现{@code toJSONString()}方法来改变转JSON字符串的方式。
 *
 * @author Looly
 *
 */
@FunctionalInterface
public interface JSONStringer extends Wrapper<Object> {

	/**
	 * 自定义转JSON字符串的方法
	 *
	 * @return JSON字符串
	 */
	String toJSONString();

	/**
	 * 获取原始的对象，默认为this
	 *
	 * @return 原始对象
	 */
	@Override
	default Object getRaw() {
		return this;
	}
}
