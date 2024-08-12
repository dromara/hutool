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

package org.dromara.hutool.log.level;

/**
 * INFO级别日志接口
 * @author Looly
 *
 */
public interface InfoLog {
	/**
	 * @return INFO 等级是否开启
	 */
	boolean isInfoEnabled();

	/**
	 * 打印 INFO 等级的日志
	 *
	 * @param t 错误对象
	 */
	void info(Throwable t);

	/**
	 * 打印 INFO 等级的日志
	 *
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void info(String format, Object... arguments);

	/**
	 * 打印 INFO 等级的日志
	 *
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void info(Throwable t, String format, Object... arguments);

	/**
	 * 打印 INFO 等级的日志
	 *
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void info(String fqcn, Throwable t, String format, Object... arguments);
}
