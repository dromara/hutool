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
			segment.reset(new StringReader(StrUtil.toStringOrEmpty(text)));
		} catch (final IOException e) {
			throw new TokenizerException(e);
		}
		return new JcsegResult(segment);
	}

}
