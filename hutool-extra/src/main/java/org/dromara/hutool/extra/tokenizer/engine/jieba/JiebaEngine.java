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
		return new JiebaResult(jiebaSegmenter.process(StrUtil.str(text), mode));
	}

}
