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

package org.dromara.hutool.log.engine.log4j;

import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 * @author Looly
 *
 */
public class Log4jLogFactory extends LogFactory{

	public Log4jLogFactory() {
		super("Log4j");
		checkLogExist(org.apache.log4j.Logger.class);
	}

	@Override
	public Log createLog(final String name) {
		return new Log4jLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new Log4jLog(clazz);
	}

}
