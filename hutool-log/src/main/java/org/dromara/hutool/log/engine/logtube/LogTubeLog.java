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

package org.dromara.hutool.log.engine.logtube;

import org.dromara.hutool.core.exceptions.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.AbstractLog;
import org.dromara.hutool.log.level.Level;
import io.github.logtube.Logtube;
import io.github.logtube.core.IEventLogger;

/**
 * <a href="https://github.com/logtube/logtube-java">LogTube</a> log.封装<br>
 *
 * @author looly
 * @since 5.6.6
 */
public class LogTubeLog extends AbstractLog {
	private static final long serialVersionUID = 1L;

	private final IEventLogger logger;

	// ------------------------------------------------------------------------- Constructor
	public LogTubeLog(final IEventLogger logger) {
		this.logger = logger;
	}

	public LogTubeLog(final Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	public LogTubeLog(final String name) {
		this(Logtube.getLogger(name));
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
		log(fqcn, Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.DEBUG, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.ERROR, t, format, arguments);
	}

	@Override
	public void log(final String fqcn, final Level level, final Throwable t, final String format, final Object... arguments) {
		final String topic = level.name().toLowerCase();
		logger.topic(topic)
				.xStackTraceElement(ExceptionUtil.getStackElement(6), null)
				.message(StrUtil.format(format, arguments))
				.xException(t)
				.commit();
	}
}
