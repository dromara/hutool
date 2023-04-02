/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.tokenizer.engine.mynlp;

import com.mayabot.nlp.segment.WordTerm;

import org.dromara.hutool.tokenizer.Word;

/**
 * mmseg分词中的一个单词包装
 *
 * @author looly
 */
public class MynlpWord implements Word {
	private static final long serialVersionUID = 1L;

	private final WordTerm word;

	/**
	 * 构造
	 *
	 * @param word {@link WordTerm}
	 */
	public MynlpWord(final WordTerm word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getWord();
	}

	@Override
	public int getStartOffset() {
		return this.word.offset;
	}

	@Override
	public int getEndOffset() {
		return getStartOffset() + word.word.length();
	}

	@Override
	public String toString() {
		return getText();
	}
}
