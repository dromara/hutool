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
		return new HanLPResult(this.seg.seg(StrUtil.str(text)));
	}

}
