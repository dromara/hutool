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
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎<br>
	 * 对不同引擎个性化配置，使用对应的{@link ClientConfig} 子类：
	 *
	 * <ul>
	 *     <li>HttpClient4和HttpClient5使用{@link org.dromara.hutool.http.client.HttpClientConfig}</li>
	 *     <li>OkHttp使用{@link org.dromara.hutool.http.client.engine.okhttp.OkHttpClientConfig}</li>
	 * </ul>
	 * <p>
	 * 如果混用这些配置，则个性配置不生效
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
		// JdkClientEngine托底，始终不空
		final ClientEngine engine = SpiUtil.loadFirstAvailable(ClientEngine.class);
		LogUtil.debug("Use [{}] Http Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}
}
