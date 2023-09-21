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

package org.dromara.hutool.core.lang.mutable;

/**
 * test for {@link MutableObj}
 *
 * @author huangchengxing
 */
class MutableObjTest extends BaseMutableTest<String, MutableObj<String>> {

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	String getValue1() {
		return "test1";
	}

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	String getValue2() {
		return "test2";
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableObj<String> getMutable(String value) {
		return new MutableObj<>(value);
	}
}
