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
