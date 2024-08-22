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

package org.dromara.hutool.extra.tokenizer.engine.ikanalyzer;

import org.dromara.hutool.extra.tokenizer.AbstractResult;
import org.dromara.hutool.extra.tokenizer.TokenizerException;
import org.dromara.hutool.extra.tokenizer.Word;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

/**
 * IKAnalyzer分词结果实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer
 *
 * @author looly
 *
 */
public class IKAnalyzerResult extends AbstractResult {

	private final IKSegmenter seg;

	/**
	 * 构造
	 *
	 * @param seg 分词结果
	 */
	public IKAnalyzerResult(final IKSegmenter seg) {
		this.seg = seg;
	}

	@Override
	protected Word nextWord() {
		final Lexeme next;
		try {
			next = this.seg.next();
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		if (null == next) {
			return null;
		}
		return new IKAnalyzerWord(next);
	}
}
