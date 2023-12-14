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
