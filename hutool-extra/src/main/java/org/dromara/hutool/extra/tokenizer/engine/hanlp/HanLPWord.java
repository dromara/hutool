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

package org.dromara.hutool.extra.tokenizer.engine.hanlp;

import com.hankcs.hanlp.seg.common.Term;

import org.dromara.hutool.extra.tokenizer.Word;

/**
 * HanLP分词中的一个单词包装
 *
 * @author looly
 *
 */
public class HanLPWord implements Word {
	private static final long serialVersionUID = 1L;

	private final Term term;

	/**
	 * 构造
	 *
	 * @param term {@link Term}
	 */
	public HanLPWord(final Term term) {
		this.term = term;
	}

	@Override
	public String getText() {
		return term.word;
	}

	@Override
	public int getStartOffset() {
		return this.term.offset;
	}

	@Override
	public int getEndOffset() {
		return getStartOffset() + this.term.length();
	}

	@Override
	public String toString() {
		return getText();
	}
}
