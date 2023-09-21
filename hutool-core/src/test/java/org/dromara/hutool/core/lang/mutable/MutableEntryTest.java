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

import org.dromara.hutool.core.map.MapUtil;

import java.util.Map;

/**
 * @author huangchengxing
 */
public class MutableEntryTest extends BaseMutableTest<Map.Entry<String, String>, MutableEntry<String, String>> {

	private static final Map.Entry<String, String> ENTRY1 = MapUtil.entry("key1", "value1");
	private static final Map.Entry<String, String> ENTRY2 = MapUtil.entry("key2", "value2");

	/**
	 * 创建一个值，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	Map.Entry<String, String> getValue1() {
		return ENTRY1;
	}

	/**
	 * 创建一个值，与{@link #getValue1()}不同，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	Map.Entry<String, String> getValue2() {
		return ENTRY2;
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableEntry<String, String> getMutable(Map.Entry<String, String> value) {
		return new MutableEntry<>(value.getKey(), value.getValue());
	}
}
