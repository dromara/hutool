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

import org.dromara.hutool.core.lang.caller.CallerUtil;
import org.dromara.hutool.log.level.DebugLog;
import org.dromara.hutool.log.level.ErrorLog;
import org.dromara.hutool.log.level.InfoLog;
import org.dromara.hutool.log.level.Level;
import org.dromara.hutool.log.level.TraceLog;
import org.dromara.hutool.log.level.WarnLog;

/**
 * 日志统一接口
 *
 * @author Looly
 *
 */
public interface Log extends TraceLog, DebugLog, InfoLog, WarnLog, ErrorLog {

	// region Static method
	/**
	 * 获得Log
	 *
	 * @param clazz 日志发出的类
	 * @return Log
	 */
	static Log get(final Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}

	/**
	 * 获得Log
	 *
	 * @param name 自定义的日志发出者名称
	 * @return Log
	 * @since 5.0.0
	 */
	static Log get(final String name) {
		return LogFactory.getLog(name);
	}

	/**
	 * @return 获得日志，自动判定日志发出者
	 * @since 5.0.0
	 */
	static Log get() {
		return LogFactory.getLog(CallerUtil.getCallerCaller());
	}
	// endregion

	/**
	 * @return 日志对象的Name
	 */
	String getName();

	/**
	 * 是否开启指定日志
	 * @param level 日志级别
	 * @return 是否开启指定级别
	 */
	boolean isEnabled(Level level);

	/**
	 * 打印指定级别的日志
	 * @param level 级别
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(Level level, String format, Object... arguments);

	/**
	 * 打印 指定级别的日志
	 *
	 * @param level 级别
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(Level level, Throwable t, String format, Object... arguments);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param level 级别
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(String fqcn, Level level, Throwable t, String format, Object... arguments);
}
