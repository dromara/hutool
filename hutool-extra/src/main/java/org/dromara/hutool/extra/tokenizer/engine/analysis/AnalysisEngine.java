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

package org.dromara.hutool.extra.tokenizer.engine.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.TokenizerException;

/**
 * Lucene-analysis分词抽象封装<br>
 * 项目地址：https://github.com/apache/lucene-solr/tree/master/lucene/analysis
 *
 * @author looly
 *
 */
public class AnalysisEngine implements TokenizerEngine {

	private final Analyzer analyzer;

	/**
	 * 构造
	 *
	 * @param analyzer 分析器{@link Analyzer}
	 */
	public AnalysisEngine(final Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public Result parse(final CharSequence text) {
		final TokenStream stream;
		try {
			stream = analyzer.tokenStream("text", StrUtil.toStringOrEmpty(text));
			stream.reset();
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		return new AnalysisResult(stream);
	}

}
