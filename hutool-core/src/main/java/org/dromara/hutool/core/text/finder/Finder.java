/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.text.finder;

/**
 * 字符串查找接口，通过调用{@link #start(int)}查找开始位置，再调用{@link #end(int)}找结束位置
 *
 * @author looly
 * @since 5.7.14
 */
public interface Finder {

	/**
	 * 未找到的的位置表示，用-1表示
	 */
	int INDEX_NOT_FOUND = -1;

	/**
	 * 返回开始位置，即起始字符位置（包含），未找到返回-1
	 *
	 * @param from 查找的开始位置（包含）
	 * @return 起始字符位置，未找到返回-1
	 */
	int start(int from);

	/**
	 * 返回结束位置，即最后一个字符后的位置（不包含）
	 *
	 * @param start 找到的起始位置
	 * @return 结束位置，未找到返回-1
	 */
	int end(int start);

	/**
	 * 复位查找器，用于重用对象
	 *
	 * @return this
	 */
	default Finder reset() {
		return this;
	}
}
