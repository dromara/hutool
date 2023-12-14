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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.TokenizerException;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;
import java.io.StringReader;

/**
 * Jcseg分词引擎实现<br>
 * 项目地址：https://gitee.com/lionsoul/jcseg<br>
 * {@link ISegment}非线程安全，每次单独创建
 *
 * @author looly
 */
public class JcsegEngine implements TokenizerEngine {

	private final SegmenterConfig config;
	private final ADictionary dic;

	/**
	 * 构造
	 */
	public JcsegEngine() {
		// 创建SegmenterConfig分词配置实例，自动查找加载jcseg.properties配置项来初始化
		this(new SegmenterConfig(true));
	}

	/**
	 * 构造
	 *
	 * @param config {@link SegmenterConfig}
	 */
	public JcsegEngine(final SegmenterConfig config) {
		this.config = config;
		// 创建默认单例词库实现，并且按照config配置加载词库
		this.dic = DictionaryFactory.createSingletonDictionary(config);
	}

	@Override
	public Result parse(final CharSequence text) {
		// 依据给定的ADictionary和SegmenterConfig来创建ISegment
		final ISegment segment = ISegment.COMPLEX.factory.create(config, dic);
		try {
			segment.reset(new StringReader(StrUtil.str(text)));
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		return new JcsegResult(segment);
	}

}
