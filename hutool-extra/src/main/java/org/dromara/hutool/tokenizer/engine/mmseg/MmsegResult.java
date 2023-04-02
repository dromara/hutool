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

package org.dromara.hutool.tokenizer.engine.mmseg;

import org.dromara.hutool.tokenizer.AbstractResult;
import org.dromara.hutool.tokenizer.TokenizerException;
import org.dromara.hutool.tokenizer.Word;
import com.chenlb.mmseg4j.MMSeg;

import java.io.IOException;

/**
 * mmseg4j分词结果实现<br>
 * 项目地址：https://github.com/chenlb/mmseg4j-core
 *
 * @author looly
 *
 */
public class MmsegResult extends AbstractResult {

	private final MMSeg mmSeg;

	/**
	 * 构造
	 *
	 * @param mmSeg 分词结果
	 */
	public MmsegResult(final MMSeg mmSeg) {
		this.mmSeg = mmSeg;
	}

	@Override
	protected Word nextWord() {
		final com.chenlb.mmseg4j.Word next;
		try {
			next = this.mmSeg.next();
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		if (null == next) {
			return null;
		}
		return new MmsegWord(next);
	}
}
