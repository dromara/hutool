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

package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.log.LogUtil;

/**
 * Http服务器引擎工厂类
 *
 * @author Looly
 * @since 6.0.0
 */
public class ServerEngineFactory {

	/**
	 * 获得单例的ServerEngine
	 *
	 * @return 单例的ServerEngine
	 */
	public static ServerEngine getEngine() {
		return Singleton.get(ServerEngine.class.getName(), ServerEngineFactory::createEngine);
	}

	/**
	 * 根据用户引入的HTTP客户端引擎jar，自动创建对应的HTTP服务器引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎<br>
	 * 对不同引擎个性化配置，使用对应的{@link ServerConfig} 子类：
	 *
	 * <p>
	 * 如果混用这些配置，则个性配置不生效
	 *
	 * @param config Http客户端配置
	 * @return {@code ClientEngine}
	 */
	@SuppressWarnings("resource")
	public static ServerEngine createEngine(final ServerConfig config) {
		return createEngine().init(config);
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`Undertow`、`Tomcat`、`Jetty`、`SunHttpServer`
	 * @return 引擎
	 * @throws HttpException 无对应名称的引擎
	 */
	public static ServerEngine createEngine(String engineName) throws HttpException {
		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<ServerEngine> list = SpiUtil.loadList(ServerEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new HttpException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的HTTP服务器引擎jar，自动创建对应的HTTP客服务器引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code ServerEngine}
	 */
	public static ServerEngine createEngine() {
		// SunServerEngine托底，始终不空
		final ServerEngine engine = SpiUtil.loadFirstAvailable(ServerEngine.class);
		LogUtil.debug("Use [{}] Http Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}
}
