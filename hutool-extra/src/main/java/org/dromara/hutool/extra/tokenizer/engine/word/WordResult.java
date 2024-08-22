/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
