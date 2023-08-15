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

package org.dromara.hutool.extra.tokenizer.engine.jcseg;

import org.dromara.hutool.extra.tokenizer.AbstractResult;
import org.dromara.hutool.extra.tokenizer.TokenizerException;
import org.dromara.hutool.extra.tokenizer.Word;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;

import java.io.IOException;

/**
 * Jcseg分词结果包装<br>
 * 项目地址：https://gitee.com/lionsoul/jcseg
 *
 * @author looly
 *
 */
public class JcsegResult extends AbstractResult {

	private final ISegment result;

	/**
	 * 构造
	 * @param segment 分词结果
	 */
	public JcsegResult(final ISegment segment) {
		this.result = segment;
	}

	@Override
	protected Word nextWord() {
		final IWord word;
		try {
			word = this.result.next();
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		if(null == word){
			return null;
		}
		return new JcsegWord(word);
	}
}
