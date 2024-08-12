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

package org.dromara.hutool.extra.tokenizer.engine.jcseg;

import org.dromara.hutool.extra.tokenizer.Word;
import org.lionsoul.jcseg.IWord;

/**
 * Jcseg分词中的一个单词包装
 *
 * @author looly
 *
 */
public class JcsegWord implements Word {
	private static final long serialVersionUID = 1L;

	private final IWord word;

	/**
	 * 构造
	 *
	 * @param word {@link IWord}
	 */
	public JcsegWord(final IWord word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getValue();
	}

	@Override
	public int getStartOffset() {
		return word.getPosition();
	}

	@Override
	public int getEndOffset() {
		return getStartOffset() + word.getLength();
	}

	@Override
	public String toString() {
		return getText();
	}
}
