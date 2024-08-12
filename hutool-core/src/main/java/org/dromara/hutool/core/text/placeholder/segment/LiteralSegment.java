/*
 * Copyright (c) 2013-2024 Hutool Team.
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
 * 字符串模板-固定文本 Segment
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class LiteralSegment implements StrTemplateSegment {
	/**
	 * 模板中固定的一段文本
	 */
	private final String text;

	/**
	 * 构造
	 *
	 * @param text 文本
	 */
	public LiteralSegment(final String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

}
