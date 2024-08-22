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

package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 基字符串模板-基于下标的占位符 Segment
 * <p>例如，"{1}"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class IndexedPlaceholderSegment extends NamedPlaceholderSegment {
	/**
	 * 下标值
	 */
	private final int index;

	/**
	 * 构造
	 *
	 * @param idxStr           索引字符串变量
	 * @param wholePlaceholder 占位符完整文本
	 */
	public IndexedPlaceholderSegment(final String idxStr, final String wholePlaceholder) {
		super(idxStr, wholePlaceholder);
		this.index = Integer.parseInt(idxStr);
	}

	public int getIndex() {
		return index;
	}
}
