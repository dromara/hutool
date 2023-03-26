/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.extra.tokenizer.engine.ansj;

import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import cn.hutool.core.text.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;

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
		return new AnsjResult(analysis.parseStr(StrUtil.str(text)));
	}

}
