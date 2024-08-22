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
import org.dromara.hutool.core.text.replacer.ReplacerChain;

/**
 * XML的UNESCAPE
 *
 * @author looly
 * @since 5.7.2
 */
public class XmlUnescape extends ReplacerChain {
	private static final long serialVersionUID = 1L;

	/**
	 * 基础反转义符
	 */
	protected static final String[][] BASIC_UNESCAPE  = InternalEscapeUtil.invert(XmlEscape.BASIC_ESCAPE);
	/**
	 * issue#1118，新增{@code &apos;}反转义
	 */
	protected static final String[][] OTHER_UNESCAPE  = new String[][]{new String[]{"&apos;", "'"}};

	/**
	 * 构造
	 */
	public XmlUnescape() {
		addChain(new LookupReplacer(BASIC_UNESCAPE));
		addChain(new NumericEntityUnescaper());
		addChain(new LookupReplacer(OTHER_UNESCAPE));
	}
}
