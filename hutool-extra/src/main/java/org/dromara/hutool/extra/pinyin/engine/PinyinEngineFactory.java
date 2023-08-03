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

package org.dromara.hutool.extra.pinyin.engine;

import org.dromara.hutool.core.lang.Singleton;
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
		LogUtil.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
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
