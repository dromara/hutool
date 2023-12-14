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

package org.dromara.hutool.extra.tokenizer.engine.mynlp;

import com.mayabot.nlp.Mynlp;
import com.mayabot.nlp.segment.Lexer;
import com.mayabot.nlp.segment.Sentence;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;

/**
 * MYNLP 中文NLP工具包分词实现<br>
 * 项目地址：https://github.com/mayabot/mynlp/<br>
 * {@link Lexer} 线程安全
 *
 * @author looly
 */
public class MynlpEngine implements TokenizerEngine {

	private final Lexer lexer;

	/**
	 * 构造
	 */
	public MynlpEngine() {
		// CORE分词器构建器
		// 开启词性标注功能
		// 开启人名识别功能
		this.lexer = Mynlp.instance().bigramLexer();
	}

	/**
	 * 构造
	 *
	 * @param lexer 分词器接口{@link Lexer}
	 */
	public MynlpEngine(final Lexer lexer) {
		this.lexer = lexer;
	}

	@Override
	public Result parse(final CharSequence text) {
		final Sentence sentence = this.lexer.scan(StrUtil.str(text));
		return new MynlpResult(sentence);
	}

}
