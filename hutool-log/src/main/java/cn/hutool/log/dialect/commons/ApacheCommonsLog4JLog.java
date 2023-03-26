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

package cn.hutool.log.dialect.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import cn.hutool.log.dialect.log4j.Log4jLog;

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
