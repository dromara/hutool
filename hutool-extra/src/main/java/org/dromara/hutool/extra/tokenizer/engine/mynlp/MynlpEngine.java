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
		final Sentence sentence = this.lexer.scan(StrUtil.toStringOrEmpty(text));
		return new MynlpResult(sentence);
	}

}
