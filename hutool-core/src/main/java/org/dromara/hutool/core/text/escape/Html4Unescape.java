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

package org.dromara.hutool.core.text.escape;

import org.dromara.hutool.core.text.replacer.LookupReplacer;

/**
 * HTML4的UNESCAPE
 *
 * @author looly
 *
 */
public class Html4Unescape extends XmlUnescape {
	private static final long serialVersionUID = 1L;

	protected static final String[][] ISO8859_1_UNESCAPE  = InternalEscapeUtil.invert(Html4Escape.ISO8859_1_ESCAPE);
	protected static final String[][] HTML40_EXTENDED_UNESCAPE  = InternalEscapeUtil.invert(Html4Escape.HTML40_EXTENDED_ESCAPE);

	public Html4Unescape() {
		super();
		addChain(new LookupReplacer(ISO8859_1_UNESCAPE));
		addChain(new LookupReplacer(HTML40_EXTENDED_UNESCAPE));
	}
}
