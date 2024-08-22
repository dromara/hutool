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

package org.dromara.hutool.log.engine.log4j2;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 * <a href="http://logging.apache.org/log4j/2.x/index.html">Apache Log4J 2</a> log.<br>
 * @author Looly
 *
 */
public class Log4j2LogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public Log4j2LogEngine() {
		super("Log4j2");
		checkLogExist(org.apache.logging.log4j.LogManager.class);
	}

	@Override
	public Log createLog(final String name) {
		return new Log4j2Log(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new Log4j2Log(clazz);
	}

}
