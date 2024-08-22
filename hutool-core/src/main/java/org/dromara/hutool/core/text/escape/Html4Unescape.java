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

package org.dromara.hutool.core.text.escape;

import org.dromara.hutool.core.text.replacer.LookupReplacer;

/**
 * HTML4的UNESCAPE
 *
 * @author looly
 */
public class Html4Unescape extends XmlUnescape {
	private static final long serialVersionUID = 1L;

	/**
	 * ISO8859_1的UNESCAPE
	 */
	protected static final String[][] ISO8859_1_UNESCAPE = InternalEscapeUtil.invert(Html4Escape.ISO8859_1_ESCAPE);
	/**
	 * HTML40_EXTENDED的UNESCAPE
	 */
	protected static final String[][] HTML40_EXTENDED_UNESCAPE = InternalEscapeUtil.invert(Html4Escape.HTML40_EXTENDED_ESCAPE);

	/**
	 * 构造
	 */
	public Html4Unescape() {
		super();
		addChain(new LookupReplacer(ISO8859_1_UNESCAPE));
		addChain(new LookupReplacer(HTML40_EXTENDED_UNESCAPE));
	}
}
