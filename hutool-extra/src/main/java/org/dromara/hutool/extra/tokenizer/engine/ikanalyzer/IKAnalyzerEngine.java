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

import org.wltea.analyzer.core.IKSegmenter;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.Result;

/**
 * IKAnalyzer分词引擎实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer
 *
 * @author looly
 *
 */
public class IKAnalyzerEngine implements TokenizerEngine {

	private final IKSegmenter seg;

	/**
	 * 构造
	 *
	 */
	public IKAnalyzerEngine() {
		this(new IKSegmenter(null, true));
	}

	/**
	 * 构造
	 *
	 * @param seg {@link IKSegmenter}
	 */
	public IKAnalyzerEngine(final IKSegmenter seg) {
		this.seg = seg;
	}

	@Override
	public Result parse(final CharSequence text) {
		this.seg.reset(StrUtil.getReader(text));
		return new IKAnalyzerResult(this.seg);
	}
}
