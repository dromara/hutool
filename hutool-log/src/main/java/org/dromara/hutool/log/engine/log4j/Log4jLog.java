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

package org.dromara.hutool.log.engine.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.AbstractLog;

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
		log(fqcn, org.dromara.hutool.log.level.Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.log.level.Level.DEBUG, t, format, arguments);
	}
	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.log.level.Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.log.level.Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, org.dromara.hutool.log.level.Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final org.dromara.hutool.log.level.Level level, final Throwable t, final String format, final Object... arguments) {
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
