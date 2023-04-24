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
