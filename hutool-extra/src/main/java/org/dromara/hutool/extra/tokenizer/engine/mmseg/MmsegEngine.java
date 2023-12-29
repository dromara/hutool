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
