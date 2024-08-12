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

package org.dromara.hutool.extra.tokenizer.engine;

import org.dromara.hutool.extra.tokenizer.Result;

/**
 * 分词引擎接口定义，用户通过实现此接口完成特定分词引擎的适配<br>
 * 由于引擎使用单例模式，因此要求实现类保证线程安全
 *
 * @author looly
 */
public interface TokenizerEngine {

	/**
	 * 文本分词处理接口，通过实现此接口完成分词，产生分词结果
	 *
	 * @param text 需要分词的文本
	 * @return {@link Result}分词结果实现
	 */
	Result parse(CharSequence text);
}
