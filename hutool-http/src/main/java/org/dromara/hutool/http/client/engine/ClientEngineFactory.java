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

package org.dromara.hutool.http.client.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.log.LogUtil;

/**
 * Http客户端引擎工厂类
 *
 * @author looly
 * @since 6.0.0
 */
public class ClientEngineFactory {

	/**
	 * 获得单例的ClientEngine
	 *
	 * @return 单例的ClientEngine
	 */
	public static ClientEngine getEngine() {
		return Singleton.get(ClientEngine.class.getName(), ClientEngineFactory::createEngine);
	}

	/**
	 * 根据用户引入的HTTP客户端引擎jar，自动创建对应的HTTP客户端引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config Http客户端配置
	 * @return {@code ClientEngine}
	 */
	@SuppressWarnings("resource")
	public static ClientEngine createEngine(final ClientConfig config) {
		return createEngine().init(config);
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`HttpClient4`、`HttpClient5`、`OkHttp`、`JdkClient`
	 * @return 引擎
	 * @throws HttpException 无对应名称的引擎
	 */
	public static ClientEngine createEngine(String engineName) throws HttpException {
		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<ClientEngine> list = SpiUtil.loadList(ClientEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new HttpException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的HTTP客户端引擎jar，自动创建对应的HTTP客户端引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code ClientEngine}
	 */
	public static ClientEngine createEngine() {
		final ClientEngine engine = doCreateEngine();
		LogUtil.debug("Use [{}] Http Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的HTTP客户端引擎jar，自动创建对应的HTTP客户端引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code EngineFactory}
	 */
	private static ClientEngine doCreateEngine() {
		final ClientEngine engine = SpiUtil.loadFirstAvailable(ClientEngine.class);
		if (null != engine) {
			return engine;
		}

		throw new HttpException("No http jar found !Please add one of it to your project !");
	}
}
