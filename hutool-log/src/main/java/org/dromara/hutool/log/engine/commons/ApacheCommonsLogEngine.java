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

package org.dromara.hutool.log.engine.commons;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 *  Apache Commons Logging
 * @author Looly
 *
 */
public class ApacheCommonsLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public ApacheCommonsLogEngine() {
		super("Apache Common Logging");
		checkLogExist(org.apache.commons.logging.LogFactory.class);
	}

	@Override
	public Log createLog(final String name) {
		try {
			return new ApacheCommonsLog4JLog(name);
		} catch (final Exception e) {
			return new ApacheCommonsLog(name);
		}
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		try {
			return new ApacheCommonsLog4JLog(clazz);
		} catch (final Exception e) {
			return new ApacheCommonsLog(clazz);
		}
	}

	@Override
	protected void checkLogExist(final Class<?> logClassName) {
		super.checkLogExist(logClassName);
		//Commons Logging在调用getLog时才检查是否有日志实现，在此提前检查，如果没有实现则跳过之
		createLog(ApacheCommonsLogEngine.class);
	}
}
