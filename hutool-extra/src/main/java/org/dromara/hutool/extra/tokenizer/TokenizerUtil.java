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
}
