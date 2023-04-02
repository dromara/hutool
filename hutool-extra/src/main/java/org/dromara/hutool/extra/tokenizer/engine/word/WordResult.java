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

package org.dromara.hutool.extra.tokenizer.engine.word;

import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.Word;

import java.util.Iterator;
import java.util.List;

/**
 * Word分词结果实现<br>
 * 项目地址：https://github.com/ysc/word
 *
 * @author looly
 *
 */
public class WordResult implements Result{

	private final Iterator<org.apdplat.word.segmentation.Word> wordIter;

	/**
	 * 构造
	 *
	 * @param result 分词结果
	 */
	public WordResult(final List<org.apdplat.word.segmentation.Word> result) {
		this.wordIter = result.iterator();
	}

	@Override
	public boolean hasNext() {
		return this.wordIter.hasNext();
	}

	@Override
	public Word next() {
		return new WordWord(this.wordIter.next());
	}

	@Override
	public void remove() {
		this.wordIter.remove();
	}
}
