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

package org.dromara.hutool.extra.tokenizer.engine.ansj;

import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.Word;
import org.ansj.domain.Term;

import java.util.Iterator;

/**
 * Ansj分词结果实现<br>
 * 项目地址：https://github.com/NLPchina/ansj_seg
 *
 * @author looly
 */
public class AnsjResult implements Result {

	private final Iterator<Term> result;

	/**
	 * 构造
	 *
	 * @param ansjResult 分词结果
	 */
	public AnsjResult(final org.ansj.domain.Result ansjResult) {
		this.result = ansjResult.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new AnsjWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}
}
