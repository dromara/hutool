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
