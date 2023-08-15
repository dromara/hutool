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
			stream = analyzer.tokenStream("text", StrUtil.str(text));
			stream.reset();
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		return new AnalysisResult(stream);
	}

}
