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

package org.dromara.hutool.dialect.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.AbstractLog;
import org.dromara.hutool.level.Level;

/**
 * <a href="http://www.slf4j.org/">SLF4J</a> log.<br>
 * 同样无缝支持 <a href="http://logback.qos.ch/">LogBack</a>
 *
 * @author Looly
 *
 */
public class Slf4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Logger logger;
	/** 是否为 LocationAwareLogger ，用于判断是否可以传递FQCN */
	private final boolean isLocationAwareLogger;

	// ------------------------------------------------------------------------- Constructor
	public Slf4jLog(final Logger logger) {
		this.logger = logger;
		this.isLocationAwareLogger = (logger instanceof LocationAwareLogger);
	}

	public Slf4jLog(final Class<?> clazz) {
		this(getSlf4jLogger(clazz));
	}

	public Slf4jLog(final String name) {
		this(LoggerFactory.getLogger(name));
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
			if(this.isLocationAwareLogger) {
				locationAwareLog((LocationAwareLogger)this.logger, fqcn, LocationAwareLogger.TRACE_INT, t, format, arguments);
			} else {
				logger.trace(StrUtil.format(format, arguments), t);
			}
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
			if(this.isLocationAwareLogger) {
				locationAwareLog((LocationAwareLogger)this.logger, fqcn, LocationAwareLogger.DEBUG_INT, t, format, arguments);
			} else {
				logger.debug(StrUtil.format(format, arguments), t);
			}
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
			if(this.isLocationAwareLogger) {
				locationAwareLog((LocationAwareLogger)this.logger, fqcn, LocationAwareLogger.INFO_INT, t, format, arguments);
			} else {
				logger.info(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isWarnEnabled()) {
			if(this.isLocationAwareLogger) {
				locationAwareLog((LocationAwareLogger)this.logger, fqcn, LocationAwareLogger.WARN_INT, t, format, arguments);
			} else {
				logger.warn(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		if (isErrorEnabled()) {
			if(this.isLocationAwareLogger) {
				locationAwareLog((LocationAwareLogger)this.logger, fqcn, LocationAwareLogger.ERROR_INT, t, format, arguments);
			} else {
				logger.error(StrUtil.format(format, arguments), t);
			}
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

	// -------------------------------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 *
	 * @param logger {@link LocationAwareLogger} 实现
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于纠正定位错误行号
	 * @param level_int 日志级别，使用LocationAwareLogger中的常量
	 * @param t 异常
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 */
	private void locationAwareLog(final LocationAwareLogger logger, final String fqcn, final int level_int, final Throwable t, final String msgTemplate, final Object[] arguments) {
		// ((LocationAwareLogger)this.logger).log(null, fqcn, level_int, msgTemplate, arguments, t);
		// 由于slf4j-log4j12中此方法的实现存在bug，故在此拼接参数
		logger.log(null, fqcn, level_int, StrUtil.format(msgTemplate, arguments), null, t);
	}

	/**
	 * 获取Slf4j Logger对象
	 *
	 * @param clazz 打印日志所在类，当为{@code null}时使用“null”表示
	 * @return {@link Logger}
	 */
	private static Logger getSlf4jLogger(final Class<?> clazz) {
		return (null == clazz) ? LoggerFactory.getLogger(StrUtil.EMPTY) : LoggerFactory.getLogger(clazz);
	}
}
