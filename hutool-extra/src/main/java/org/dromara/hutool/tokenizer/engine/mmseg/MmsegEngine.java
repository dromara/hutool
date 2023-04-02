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

import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.tokenizer.Result;
import org.dromara.hutool.tokenizer.TokenizerEngine;
import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;

import java.io.StringReader;

/**
 * mmseg4j分词引擎实现<br>
 * 项目地址：https://github.com/chenlb/mmseg4j-core
 *
 * @author looly
 *
 */
public class MmsegEngine implements TokenizerEngine {

	private final MMSeg mmSeg;

	/**
	 * 构造
	 */
	public MmsegEngine() {
		final Dictionary dict = Dictionary.getInstance();
		final ComplexSeg seg = new ComplexSeg(dict);
		this.mmSeg = new MMSeg(new StringReader(""), seg);
	}

	/**
	 * 构造
	 *
	 * @param mmSeg 模式{@link MMSeg}
	 */
	public MmsegEngine(final MMSeg mmSeg) {
		this.mmSeg = mmSeg;
	}

	@Override
	public Result parse(final CharSequence text) {
		this.mmSeg.reset(StrUtil.getReader(text));
		return new MmsegResult(this.mmSeg);
	}

}
