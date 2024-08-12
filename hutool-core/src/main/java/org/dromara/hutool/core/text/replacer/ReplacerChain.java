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

package org.dromara.hutool.core.text.replacer;

import org.dromara.hutool.core.lang.Chain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 字符串替换链，用于组合多个字符串替换逻辑
 *
 * @author looly
 * @since 4.1.5
 */
public class ReplacerChain extends StrReplacer implements Chain<StrReplacer, ReplacerChain> {
	private static final long serialVersionUID = 1L;

	private final List<StrReplacer> replacers = new LinkedList<>();

	/**
	 * 构造
	 *
	 * @param strReplacers 字符串替换器
	 */
	public ReplacerChain(final StrReplacer... strReplacers) {
		for (final StrReplacer strReplacer : strReplacers) {
			addChain(strReplacer);
		}
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Iterator<StrReplacer> iterator() {
		return replacers.iterator();
	}

	@Override
	public ReplacerChain addChain(final StrReplacer element) {
		replacers.add(element);
		return this;
	}

	@Override
	protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
		int consumed = 0;
		for (final StrReplacer strReplacer : replacers) {
			consumed = strReplacer.replace(str, pos, out);
			if (0 != consumed) {
				return consumed;
			}
		}
		return consumed;
	}

}
