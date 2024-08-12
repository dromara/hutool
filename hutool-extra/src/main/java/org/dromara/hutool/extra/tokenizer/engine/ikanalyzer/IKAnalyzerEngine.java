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

package org.dromara.hutool.extra.tokenizer.engine.ikanalyzer;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.Result;

/**
 * IKAnalyzer分词引擎实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer<br>
 * {@link IKSegmenter} 非线程全，因此每次单独创建对象
 *
 * @author looly
 */
public class IKAnalyzerEngine implements TokenizerEngine {

	private final Configuration cfg;

	/**
	 * 构造
	 */
	public IKAnalyzerEngine() {
		this(createDefaultConfig());
	}

	/**
	 * 构造
	 * @param cfg 配置
	 */
	public IKAnalyzerEngine(final Configuration cfg) {
		cfg.setUseSmart(true);
		this.cfg = cfg;
	}

	@Override
	public Result parse(final CharSequence text) {
		final IKSegmenter seg = new IKSegmenter(StrUtil.getReader(text), cfg);
		return new IKAnalyzerResult(seg);
	}

	/**
	 * 创建默认配置
	 * @return {@link Configuration}
	 */
	private static Configuration createDefaultConfig(){
		final Configuration configuration = DefaultConfig.getInstance();
		configuration.setUseSmart(true);
		return configuration;
	}
}
