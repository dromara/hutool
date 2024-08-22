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
		return new WordResult(this.segmentation.seg(StrUtil.toStringOrEmpty(text)));
	}

}
