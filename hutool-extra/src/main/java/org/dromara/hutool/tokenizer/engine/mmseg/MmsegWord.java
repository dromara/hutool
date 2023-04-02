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

package org.dromara.hutool.tokenizer.engine.mmseg;

import org.dromara.hutool.tokenizer.Word;

/**
 * mmseg分词中的一个单词包装
 *
 * @author looly
 *
 */
public class MmsegWord implements Word {
	private static final long serialVersionUID = 1L;

	private final com.chenlb.mmseg4j.Word word;

	/**
	 * 构造
	 *
	 * @param word {@link com.chenlb.mmseg4j.Word}
	 */
	public MmsegWord(final com.chenlb.mmseg4j.Word word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getString();
	}

	@Override
	public int getStartOffset() {
		return this.word.getStartOffset();
	}

	@Override
	public int getEndOffset() {
		return this.word.getEndOffset();
	}

	@Override
	public String toString() {
		return getText();
	}
}
