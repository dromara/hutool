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
 * @author huangchengxing
 */
public class MutableTripleTest extends BaseMutableTest<MutableTriple<String, String, String>, MutableTriple<String, String, String>> {

	private static final MutableTriple<String, String, String> VALUE1 = new MutableTriple<>("1", "2", "3");
	private static final MutableTriple<String, String, String> VALUE2 = new MutableTriple<>("4", "5", "6");

	/**
	 * 创建一个值，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	MutableTriple<String, String, String> getValue1() {
		return VALUE1;
	}

	/**
	 * 创建一个值，与{@link #getValue1()}不同，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	MutableTriple<String, String, String> getValue2() {
		return VALUE2;
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableTriple<String, String, String> getMutable(MutableTriple<String, String, String> value) {
		return new MutableTriple<>(value.getLeft(), value.getMiddle(), value.getRight());
	}
}
