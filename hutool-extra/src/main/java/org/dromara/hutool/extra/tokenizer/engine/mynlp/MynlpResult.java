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

package org.dromara.hutool.extra.tokenizer.engine.mynlp;

import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.Word;
import com.mayabot.nlp.segment.Sentence;
import com.mayabot.nlp.segment.WordTerm;

import java.util.Iterator;

/**
 * MYNLP 中文NLP工具包分词结果实现<br>
 * 项目地址：https://github.com/mayabot/mynlp/
 *
 * @author looly
 *
 */
public class MynlpResult implements Result {

	private final Iterator<WordTerm> result;

	/**
	 * 构造
	 *
	 * @param sentence 分词结果（中文句子）
	 */
	public MynlpResult(final Sentence sentence) {
		this.result = sentence.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new MynlpWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}
}
