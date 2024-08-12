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

import java.io.Serializable;

/**
 * 表示分词中的一个词
 *
 * @author looly
 *
 */
public interface Word extends Serializable {

	/**
	 * 获取单词文本
	 *
	 * @return 单词文本
	 */
	String getText();

	/**
	 * 获取本词的起始位置
	 *
	 * @return 起始位置
	 */
	int getStartOffset();

	/**
	 * 获取本词的结束位置
	 *
	 * @return 结束位置
	 */
	int getEndOffset();
}
