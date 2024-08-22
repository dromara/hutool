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

package org.dromara.hutool.extra.tokenizer.engine.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.Result;

/**
 * HanLP分词引擎实现<br>
 * 项目地址：https://github.com/hankcs/HanLP<br>
 * {@link Segment#seg(String)}方法线程安全
 *
 * @author looly
 */
public class HanLPEngine implements TokenizerEngine {

	private final Segment seg;

	/**
	 * 构造
	 */
	public HanLPEngine() {
		this(HanLP.newSegment());
	}

	/**
	 * 构造
	 *
	 * @param seg {@link Segment}
	 */
	public HanLPEngine(final Segment seg) {
		this.seg = seg;
	}

	@Override
	public Result parse(final CharSequence text) {
		return new HanLPResult(this.seg.seg(StrUtil.toStringOrEmpty(text)));
	}

}
