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

package org.dromara.hutool.log.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.log.Log;

/**
 * 日期引擎接口
 *
 * @author looly
 * @since 6.0.0
 */
public interface LogEngine {

	/**
	 * 获取日志框架名，用于打印当前所用日志框架
	 *
	 * @return 日志框架名
	 */
	String getName();

	/**
	 * 创建日志对象
	 *
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	Log createLog(String name);

	/**
	 * 创建日志对象
	 *
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	Log createLog(Class<?> clazz);

	/**
	 * 获得日志对象（单例）
	 *
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	default Log getLog(final String name) {
		// 将引擎名称和日期名称关联，以便在引擎更换后重新获取引擎。
		return Singleton.get(getName() + name, () -> createLog(name));
	}

	/**
	 * 获得日志对象（单例）
	 *
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	default Log getLog(final Class<?> clazz) {
		// 将引擎名称和日期名称关联，以便在引擎更换后重新获取引擎。
		return Singleton.get(getName() + clazz.getName(), () -> createLog(clazz));
	}
}
