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
