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

package org.dromara.hutool.tokenizer.engine.jieba;

import com.huaban.analysis.jieba.SegToken;

import org.dromara.hutool.tokenizer.Word;

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
