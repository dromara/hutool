/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.ServiceLoader;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;

/**
 * JSON引擎工厂
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
	 * @param engineName 引擎名称，忽略大小写，如`HttpClient4`、`HttpClient5`、`OkHttp`、`JdkClient`
	 * @return 引擎
	 * @throws JSONException 无对应名称的引擎
	 */
	public static JSONEngine createEngine(String engineName) throws JSONException {
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
	 * 根据用户引入的JSON引擎jar，自动创建对应的JSON引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code ClientEngine}
	 */
	public static JSONEngine createEngine() {
		// HutoolJSONEngine托底，始终不空
		return SpiUtil.loadFirstAvailable(JSONEngine.class);
	}
}
