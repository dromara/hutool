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
 * test for {@link MutableBool}
 *
 * @author huangchengxing
 */
public class MutableBoolTest extends BaseMutableTest<Boolean, MutableBool> {

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	Boolean getValue1() {
		return Boolean.TRUE;
	}

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	Boolean getValue2() {
		return Boolean.FALSE;
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableBool getMutable(Boolean value) {
		return new MutableBool(value);
	}
}
