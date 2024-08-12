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

package org.dromara.hutool.extra.tokenizer;

import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngineFactory;

/**
 * 分词工具类
 *
 * @author looly
 * @since 4.3.3
 */
public class TokenizerUtil {

	/**
	 * 分词处理
	 *
	 * @param text 文本
	 * @return 分词结果
	 */
	public static Result parse(final String text) {
		return getEngine().parse(text);
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 *
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine getEngine() {
		return TokenizerEngineFactory.getEngine();
	}

	/**
	 * 创建对应名称的分词引擎对象
	 *
	 * @param engineName 引擎名称
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine createEngine(final String engineName) {
		return TokenizerEngineFactory.createEngine(engineName);
	}
}
