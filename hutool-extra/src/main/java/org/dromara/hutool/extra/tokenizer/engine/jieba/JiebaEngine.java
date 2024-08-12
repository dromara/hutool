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

package org.dromara.hutool.extra.tokenizer.engine.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.Result;

/**
 * Jieba分词引擎实现<br>
 * 项目地址：https://github.com/huaban/jieba-analysis
 * {@link JiebaSegmenter#process(String, SegMode)} 线程安全
 *
 * @author looly
 *
 */
public class JiebaEngine implements TokenizerEngine {

	private final JiebaSegmenter jiebaSegmenter;
	private final SegMode mode;

	/**
	 * 构造
	 */
	public JiebaEngine() {
		this(SegMode.SEARCH);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link SegMode}
	 */
	public JiebaEngine(final SegMode mode) {
		this.jiebaSegmenter = new JiebaSegmenter();
		this.mode = mode;
	}

	@Override
	public Result parse(final CharSequence text) {
		return new JiebaResult(jiebaSegmenter.process(StrUtil.toStringOrEmpty(text), mode));
	}

}
