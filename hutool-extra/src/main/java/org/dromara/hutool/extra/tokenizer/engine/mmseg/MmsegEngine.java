/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import com.chenlb.mmseg4j.Seg;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;

/**
 * mmseg4j分词引擎实现<br>
 * 项目地址：https://github.com/chenlb/mmseg4j-core<br>
 * {@link MMSeg}非线程安全，故单独创建之
 *
 * @author looly
 */
public class MmsegEngine implements TokenizerEngine {

	private final Seg seg;

	/**
	 * 构造
	 */
	public MmsegEngine() {
		this(new ComplexSeg(Dictionary.getInstance()));
	}

	/**
	 * 构造
	 *
	 * @param seg 模式{@link Seg}
	 */
	public MmsegEngine(final Seg seg) {
		this.seg = seg;
	}

	@Override
	public Result parse(final CharSequence text) {
		final MMSeg mmSeg = new MMSeg(StrUtil.getReader(text), seg);
		return new MmsegResult(mmSeg);
	}

}
