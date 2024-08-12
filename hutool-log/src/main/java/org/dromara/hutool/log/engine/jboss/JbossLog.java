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

package org.dromara.hutool.log.engine.jboss;

import org.jboss.logging.Logger;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.AbstractLog;
import org.dromara.hutool.log.level.Level;

/**
 * <a href="https://github.com/jboss-logging">Jboss-Logging</a> log.
 *
 * @author Looly
 *
 */
public class JbossLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	/**
	 * 构造
	 *
	 * @param logger {@link Logger}
	 */
	public JbossLog(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * 构造
	 *
	 * @param clazz 日志打印所在类
	 */
	public JbossLog(final Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	/**
	 * 构造
	 *
	 * @param name 日志打印所在类名
	 */
	public JbossLog(final String name) {
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
		if (isTraceEnabled()) {
			logger.trace(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isDebugEnabled()) {
			logger.debug(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isInfoEnabled()) {
			logger.info(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabled(Logger.Level.WARN);
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isWarnEnabled()) {
			logger.warn(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabled(Logger.Level.ERROR);
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isErrorEnabled()) {
			logger.error(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final Level level, final Throwable t, final String format, final Object... arguments) {
		switch (level) {
		case TRACE:
			trace(fqcn, t, format, arguments);
			break;
		case DEBUG:
			debug(fqcn, t, format, arguments);
			break;
		case INFO:
			info(fqcn, t, format, arguments);
			break;
		case WARN:
			warn(fqcn, t, format, arguments);
			break;
		case ERROR:
			error(fqcn, t, format, arguments);
			break;
		default:
			throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}
}
