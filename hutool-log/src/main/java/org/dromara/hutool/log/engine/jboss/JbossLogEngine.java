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

package org.dromara.hutool.log.engine.jboss;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 * <a href="https://github.com/jboss-logging">Jboss-Logging</a> log.
 *
 * @author Looly
 * @since 4.1.21
 */
public class JbossLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public JbossLogEngine() {
		super("JBoss Logging");
		checkLogExist(org.jboss.logging.Logger.class);
	}

	@Override
	public Log createLog(final String name) {
		return new JbossLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new JbossLog(clazz);
	}

}
