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

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;

/**
 * JSON引擎工厂<br>
 * 通过SPI方式，动态查找用户引入的JSON实现库，并加载，提供两种加载方式：
 * <ul>
 *     <li>{@link #getEngine()} 自动按照service文件中的顺序检查并加载第一个可用引擎。</li>
 *     <li>{@link #createEngine(String)} 加载指定名称的引擎</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONEngineFactory {

	/**
	 * 获得单例的ClientEngine
	 *
	 * @return 单例的ClientEngine
	 */
	public static JSONEngine getEngine() {
		return Singleton.get(JSONEngine.class.getName(), JSONEngineFactory::createEngine);
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`FastJSON2`、`Jackson`、`Gson`、`HutoolJSON`
	 * @return 引擎
	 * @throws JSONException 无对应名称的引擎
	 */
	public static JSONEngine createEngine(String engineName) throws JSONException {
		// fastjson名字兼容
		if (StrUtil.equalsIgnoreCase("fastjson", engineName)) {
			engineName = "FastJSON2";
		}
		if (StrUtil.equalsIgnoreCase("hutool", engineName)) {
			engineName = "HutoolJSON";
		}

		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<JSONEngine> list = SpiUtil.loadList(JSONEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new JSONException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的JSON引擎jar，自动创建对应的HTTP客户端引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config JSON引擎配置
	 * @return {@code JSONEngine}
	 */
	public static JSONEngine createEngine(final JSONEngineConfig config) {
		return createEngine().init(config);
	}

	/**
	 * 根据用户引入的JSON引擎jar，自动创建对应的JSON引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code JSONEngine}
	 */
	public static JSONEngine createEngine() {
		// HutoolJSONEngine托底，始终不空
		return SpiUtil.loadFirstAvailable(JSONEngine.class);
	}
}
