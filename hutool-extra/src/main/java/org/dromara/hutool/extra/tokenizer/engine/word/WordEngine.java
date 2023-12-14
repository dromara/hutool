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

package org.dromara.hutool.extra.tokenizer.engine.word;

import org.apdplat.word.segmentation.Segmentation;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.SegmentationFactory;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;

/**
 * Word分词引擎实现<br>
 * 项目地址：https://github.com/ysc/word<br>
 * {@link Segmentation} 线程安全
 *
 * @author looly
 *
 */
public class WordEngine implements TokenizerEngine {

	private final Segmentation segmentation;

	/**
	 * 构造
	 */
	public WordEngine() {
		this(SegmentationAlgorithm.BidirectionalMaximumMatching);
	}

	/**
	 * 构造
	 *
	 * @param algorithm {@link SegmentationAlgorithm}分词算法枚举
	 */
	public WordEngine(final SegmentationAlgorithm algorithm) {
		this(SegmentationFactory.getSegmentation(algorithm));
	}

	/**
	 * 构造
	 *
	 * @param segmentation {@link Segmentation}分词实现
	 */
	public WordEngine(final Segmentation segmentation) {
		this.segmentation = segmentation;
	}

	@Override
	public Result parse(final CharSequence text) {
		return new WordResult(this.segmentation.seg(StrUtil.str(text)));
	}

}
