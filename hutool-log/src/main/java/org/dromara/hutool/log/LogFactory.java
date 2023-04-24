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

package org.dromara.hutool.log;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.log.engine.LogEngineFactory;

/**
 * 日志简单工厂类，提供带有缓存的日志对象创建
 *
 * @author Looly
 */
public abstract class LogFactory {

	/**
	 * 获得日志对象
	 *
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	public static Log getLog(final String name) {
		return Singleton.get(name, () -> LogEngineFactory.getEngine().createLog(name));
	}

	/**
	 * 获得日志对象
	 *
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public static Log getLog(final Class<?> clazz) {
		return Singleton.get(clazz.getName(), () -> LogEngineFactory.getEngine().createLog(clazz));
	}
}
