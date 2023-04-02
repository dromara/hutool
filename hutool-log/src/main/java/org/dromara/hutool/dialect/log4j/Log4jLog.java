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

package org.dromara.hutool.dialect.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.AbstractLog;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 *
 * @author Looly
 *
 */
public class Log4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Log4jLog(final Logger logger) {
		this.logger = logger;
	}

	public Log4jLog(final Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	public Log4jLog(final String name) {
		this(Logger.getLogger(name));
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.level.Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.level.Level.DEBUG, t, format, arguments);
	}
	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.level.Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.level.Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.level.Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final org.dromara.hutool.level.Level level, final Throwable t, final String format, final Object... arguments) {
		final Level log4jLevel;
		switch (level) {
			case TRACE:
				log4jLevel = Level.TRACE;
				break;
			case DEBUG:
				log4jLevel = Level.DEBUG;
				break;
			case INFO:
				log4jLevel = Level.INFO;
				break;
			case WARN:
				log4jLevel = Level.WARN;
				break;
			case ERROR:
				log4jLevel = Level.ERROR;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}

		if(logger.isEnabledFor(log4jLevel)) {
			logger.log(fqcn, log4jLevel, StrUtil.format(format, arguments), t);
		}
	}
}
