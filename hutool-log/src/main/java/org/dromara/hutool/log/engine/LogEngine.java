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
