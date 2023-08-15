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

package org.dromara.hutool.extra.tokenizer.engine.analysis;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Attribute;

import org.dromara.hutool.extra.tokenizer.Word;

/**
 * Lucene-analysis分词中的一个单词包装
 *
 * @author looly
 *
 */
public class AnalysisWord implements Word {
	private static final long serialVersionUID = 1L;

	private final Attribute word;

	/**
	 * 构造
	 *
	 * @param word {@link CharTermAttribute}
	 */
	public AnalysisWord(final CharTermAttribute word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.toString();
	}

	@Override
	public int getStartOffset() {
		if(this.word instanceof OffsetAttribute) {
			return ((OffsetAttribute)this.word).startOffset();
		}
		return -1;
	}

	@Override
	public int getEndOffset() {
		if(this.word instanceof OffsetAttribute) {
			return ((OffsetAttribute)this.word).endOffset();
		}
		return -1;
	}

	@Override
	public String toString() {
		return getText();
	}
}
