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

package org.dromara.hutool.log.engine.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import org.dromara.hutool.log.engine.log4j.Log4jLog;

/**
 * Apache Commons Logging for Log4j
 *
 * @author Looly
 */
public class ApacheCommonsLog4JLog extends Log4jLog {
	private static final long serialVersionUID = -6843151523380063975L;

	// ------------------------------------------------------------------------- Constructor

	/**
	 * 构造
	 *
	 * @param logger Logger
	 */
	public ApacheCommonsLog4JLog(final Log logger) {
		super(((Log4JLogger) logger).getLogger());
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ApacheCommonsLog4JLog(final Class<?> clazz) {
		super(clazz);
	}

	/**
	 * 构造
	 *
	 * @param name 名称
	 */
	public ApacheCommonsLog4JLog(final String name) {
		super(name);
	}
}
