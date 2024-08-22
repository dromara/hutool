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
 * 字符串模板-占位符-抽象 Segment
 * <p>例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public abstract class AbstractPlaceholderSegment implements StrTemplateSegment {
	/**
	 * 占位符变量，例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}
	 */
	private final String placeholder;

	/**
	 * 构造
	 *
	 * @param placeholder 占位符变量，例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}
	 */
	protected AbstractPlaceholderSegment(final String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public String getText() {
		return placeholder;
	}

	/**
	 * 获取占位符
	 *
	 * @return 占位符
	 */
	public String getPlaceholder() {
		return placeholder;
	}
}
