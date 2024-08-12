/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.log;

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
		return LogEngineFactory.getEngine().getLog(name);
	}

	/**
	 * 获得日志对象
	 *
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public static Log getLog(final Class<?> clazz) {
		return LogEngineFactory.getEngine().getLog(clazz);
	}
}
