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

package org.dromara.hutool.extra.tokenizer.engine.jieba;

import com.huaban.analysis.jieba.SegToken;

import org.dromara.hutool.extra.tokenizer.Word;

/**
 * Jieba分词中的一个单词包装
 *
 * @author looly
 *
 */
public class JiebaWord implements Word {
	private static final long serialVersionUID = 1L;

	private final SegToken segToken;

	/**
	 * 构造
	 *
	 * @param segToken {@link SegToken}
	 */
	public JiebaWord(final SegToken segToken) {
		this.segToken = segToken;
	}

	@Override
	public String getText() {
		return segToken.word;
	}

	@Override
	public int getStartOffset() {
		return segToken.startOffset;
	}

	@Override
	public int getEndOffset() {
		return segToken.endOffset;
	}

	@Override
	public String toString() {
		return getText();
	}
}
