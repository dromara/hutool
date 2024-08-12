/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.extra.tokenizer;

import org.dromara.hutool.core.collection.iter.ComputeIter;

import java.util.Iterator;

/**
 * 对于未实现{@link Iterator}接口的普通结果类，装饰为{@link Result}<br>
 * 普通的结果类只需实现{@link #nextWord()} 即可
 *
 * @author looly
 *
 */
public abstract class AbstractResult extends ComputeIter<Word> implements Result{

	/**
	 * 下一个单词，通过实现此方法获取下一个单词，null表示无下一个结果。
	 * @return 下一个单词或null
	 */
	protected abstract Word nextWord();

	@Override
	protected Word computeNext() {
		return nextWord();
	}
}
