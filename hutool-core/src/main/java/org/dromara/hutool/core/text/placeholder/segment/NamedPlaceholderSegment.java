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
 * 字符串模板-有前后缀的变量占位符 Segment
 * <p>例如，"{1}", "{name}", "#{id}"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class NamedPlaceholderSegment extends AbstractPlaceholderSegment {
	/**
	 * 占位符完整文本
	 * <p>例如：{@literal "{name}"->"{name}"}</p>
	 */
	private final String wholePlaceholder;

	/**
	 * 构造
	 * @param name 占位符变量
	 * @param wholePlaceholder 占位符完整文本
	 */
	public NamedPlaceholderSegment(final String name, final String wholePlaceholder) {
		super(name);
		this.wholePlaceholder = wholePlaceholder;
	}

	@Override
	public String getText() {
		return wholePlaceholder;
	}

}
