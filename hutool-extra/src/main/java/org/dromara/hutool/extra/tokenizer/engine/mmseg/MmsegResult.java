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

package org.dromara.hutool.extra.tokenizer.engine.mmseg;

import org.dromara.hutool.extra.tokenizer.AbstractResult;
import org.dromara.hutool.extra.tokenizer.TokenizerException;
import org.dromara.hutool.extra.tokenizer.Word;
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
