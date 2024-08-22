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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.AbstractLog;
import org.dromara.hutool.log.level.Level;

/**
 * Apache Commons Logging
 *
 * @author Looly
 */
public class ApacheCommonsLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Log logger;
	private final String name;

	// ------------------------------------------------------------------------- Constructor

	/**
	 * 构造
	 *
	 * @param logger Logger
	 * @param name   名称
	 */
	public ApacheCommonsLog(final Log logger, final String name) {
		this.logger = logger;
		this.name = name;
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ApacheCommonsLog(final Class<?> clazz) {
		this(LogFactory.getLog(clazz), null == clazz ? StrUtil.NULL : clazz.getName());
	}

	/**
	 * 构造
	 *
	 * @param name 名称
	 */
	public ApacheCommonsLog(final String name) {
		this(LogFactory.getLog(name), name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		// fqcn此处无效
		if (isTraceEnabled()) {
			logger.trace(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		// fqcn此处无效
		if (isDebugEnabled()) {
			logger.debug(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		// fqcn此处无效
		if (isInfoEnabled()) {
			logger.info(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(final String format, final Object... arguments) {
		if (isWarnEnabled()) {
			logger.warn(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void warn(final Throwable t, final String format, final Object... arguments) {
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		// fqcn此处无效
		if (isWarnEnabled()) {
			logger.warn(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		// fqcn此处无效
		if (isErrorEnabled()) {
			logger.warn(StrUtil.format(format, arguments), t);
		}

	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final Level level, final Throwable t, final String format, final Object... arguments) {
		switch (level) {
			case TRACE:
				trace(t, format, arguments);
				break;
			case DEBUG:
				debug(t, format, arguments);
				break;
			case INFO:
				info(t, format, arguments);
				break;
			case WARN:
				warn(t, format, arguments);
				break;
			case ERROR:
				error(t, format, arguments);
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}
	// ------------------------------------------------------------------------- Private method
}
