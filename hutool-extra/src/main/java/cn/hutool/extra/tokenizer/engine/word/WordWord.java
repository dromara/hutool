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

package cn.hutool.extra.tokenizer.engine.word;

import cn.hutool.extra.tokenizer.Word;

/**
 * Word分词中的一个单词包装
 *
 * @author looly
 *
 */
public class WordWord implements Word {
	private static final long serialVersionUID = 1L;

	private final org.apdplat.word.segmentation.Word word;

	/**
	 * 构造
	 *
	 * @param word {@link org.apdplat.word.segmentation.Word}
	 */
	public WordWord(final org.apdplat.word.segmentation.Word word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getText();
	}

	@Override
	public int getStartOffset() {
		return -1;
	}

	@Override
	public int getEndOffset() {
		return -1;
	}

	@Override
	public String toString() {
		return getText();
	}
}
