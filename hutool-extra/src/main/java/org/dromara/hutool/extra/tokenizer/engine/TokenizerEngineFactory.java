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

package org.dromara.hutool.extra.tokenizer.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.tokenizer.TokenizerException;
import org.dromara.hutool.log.LogUtil;

/**
 * 简单分词引擎工厂，用于根据用户引入的分词引擎jar，自动创建对应的引擎
 *
 * @author looly
 */
public class TokenizerEngineFactory {

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的分词引擎对象<br>
	 * 获得的是单例的TokenizerEngine
	 *
	 * @return 单例的TokenizerEngine
	 */
	public static TokenizerEngine getEngine() {
		final TokenizerEngine engine = Singleton.get(TokenizerEngine.class.getName(), TokenizerEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Tokenizer Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 *
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine createEngine() {
		return doCreateEngine();
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`Analysis`、`Ansj`、`HanLP`、`IKAnalyzer`、`Jcseg`、`Jieba`、`Mmseg`、`Mynlp`、`Word`
	 * @return 引擎
	 * @throws TokenizerException 无对应名称的引擎
	 */
	public static TokenizerEngine createEngine(String engineName) throws TokenizerException {
		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<TokenizerEngine> list = SpiUtil.loadList(TokenizerEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new TokenizerException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 *
	 * @return {@link TokenizerEngine}
	 */
	private static TokenizerEngine doCreateEngine() {
		final TokenizerEngine engine = SpiUtil.loadFirstAvailable(TokenizerEngine.class);
		if (null != engine) {
			return engine;
		}

		throw new TokenizerException("No tokenizer found !Please add some tokenizer jar to your project !");
	}
}
