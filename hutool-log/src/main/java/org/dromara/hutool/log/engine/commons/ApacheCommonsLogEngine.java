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
