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

package org.dromara.hutool.extra.tokenizer.engine.ikanalyzer;

import org.wltea.analyzer.core.Lexeme;

import org.dromara.hutool.extra.tokenizer.Word;

/**
 * IKAnalyzer分词中的一个单词包装
 *
 * @author looly
 *
 */
public class IKAnalyzerWord implements Word {
	private static final long serialVersionUID = 1L;

	private final Lexeme word;

	/**
	 * 构造
	 *
	 * @param word {@link Lexeme}
	 */
	public IKAnalyzerWord(final Lexeme word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getLexemeText();
	}

	@Override
	public int getStartOffset() {
		return word.getBeginPosition();
	}

	@Override
	public int getEndOffset() {
		return word.getEndPosition();
	}

	@Override
	public String toString() {
		return getText();
	}
}
