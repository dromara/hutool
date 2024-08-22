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

package org.dromara.hutool.extra.pinyin.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.pinyin.PinyinException;
import org.dromara.hutool.log.LogUtil;

/**
 * 简单拼音引擎工厂，用于根据用户引入的拼音库jar，自动创建对应的拼音引擎对象<br>
 * 使用简单工厂（Simple Factory）模式
 *
 * @author looly
 */
public class PinyinEngineFactory {

	/**
	 * 获得单例的PinyinEngine
	 *
	 * @return 单例的PinyinEngine
	 */
	public static PinyinEngine getEngine(){
		final PinyinEngine engine = Singleton.get(PinyinEngine.class.getName(), PinyinEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Pinyin Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的拼音引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link PinyinEngine}
	 */
	public static PinyinEngine createEngine() {
		return doCreateEngine();
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`Bopomofo4j`、`Houbb`、`JPinyin`、`Pinyin4j`、`TinyPinyin`
	 * @return 引擎
	 * @throws PinyinException 无对应名称的引擎
	 */
	public static PinyinEngine createEngine(String engineName) throws PinyinException {
		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<PinyinEngine> list = SpiUtil.loadList(PinyinEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new PinyinException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的拼音引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link PinyinEngine}
	 */
	private static PinyinEngine doCreateEngine() {
		final PinyinEngine engine = SpiUtil.loadFirstAvailable(PinyinEngine.class);
		if(null != engine){
			return engine;
		}

		throw new PinyinException("No pinyin jar found !Please add one of it to your project !");
	}
}
