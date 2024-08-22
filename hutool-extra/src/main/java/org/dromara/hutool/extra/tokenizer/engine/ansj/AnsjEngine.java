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

import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;

/**
 * Ansj分词引擎实现<br>
 * 项目地址：https://github.com/NLPchina/ansj_seg
 *
 * @author looly
 *
 */
public class AnsjEngine implements TokenizerEngine {

	private final Analysis analysis;

	/**
	 * 构造
	 */
	public AnsjEngine() {
		this(new ToAnalysis());
	}

	/**
	 * 构造
	 *
	 * @param analysis {@link Analysis}
	 */
	public AnsjEngine(final Analysis analysis) {
		this.analysis = analysis;
	}

	@Override
	public Result parse(final CharSequence text) {
		return new AnsjResult(analysis.parseStr(StrUtil.toStringOrEmpty(text)));
	}

}
