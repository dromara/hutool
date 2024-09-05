/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
