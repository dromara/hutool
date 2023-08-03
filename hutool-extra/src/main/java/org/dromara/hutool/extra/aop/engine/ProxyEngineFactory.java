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

package org.dromara.hutool.extra.aop.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.LogUtil;

/**
 * 代理引擎简单工厂<br>
 * 根据用户引入代理库的不同，产生不同的代理引擎对象
 *
 * @author looly
 */
public class ProxyEngineFactory {

	/**
	 * 获得单例的ProxyEngine
	 *
	 * @return 单例的ProxyEngine
	 */
	public static ProxyEngine getEngine() {
		final ProxyEngine engine = Singleton.get(ProxyEngine.class.getName(), ProxyEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入Cglib与否创建代理工厂
	 *
	 * @return 代理工厂
	 */
	public static ProxyEngine createEngine() {
		return SpiUtil.loadFirstAvailable(ProxyEngine.class);
	}
}
