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
