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

package cn.hutool.log.dialect.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

import cn.hutool.core.text.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://logging.apache.org/log4j/2.x/index.html">Apache Log4J 2</a> log.<br>
 *
 * @author Looly
 *
 */
public class Log4j2Log extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Log4j2Log(final Logger logger) {
		this.logger = logger;
	}

	public Log4j2Log(final Class<?> clazz) {
		this(LogManager.getLogger(clazz));
	}

	public Log4j2Log(final String name) {
		this(LogManager.getLogger(name));
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
		logIfEnabled(fqcn, Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.DEBUG, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final cn.hutool.log.level.Level level, final Throwable t, final String format, final Object... arguments) {
		final Level log4j2Level;
		switch (level) {
			case TRACE:
				log4j2Level = Level.TRACE;
				break;
			case DEBUG:
				log4j2Level = Level.DEBUG;
				break;
			case INFO:
				log4j2Level = Level.INFO;
				break;
			case WARN:
				log4j2Level = Level.WARN;
				break;
			case ERROR:
				log4j2Level = Level.ERROR;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		logIfEnabled(fqcn, log4j2Level, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 *
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于纠正定位错误行号
	 * @param level 日志级别，使用org.apache.logging.log4j.Level中的常量
	 * @param t 异常
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 */
	private void logIfEnabled(final String fqcn, final Level level, final Throwable t, final String msgTemplate, final Object... arguments) {
		if(this.logger.isEnabled(level)) {
			if(this.logger instanceof AbstractLogger){
				((AbstractLogger)this.logger).logIfEnabled(fqcn, level, null, StrUtil.format(msgTemplate, arguments), t);
			} else {
				// FQCN无效
				this.logger.log(level, StrUtil.format(msgTemplate, arguments), t);
			}
		}
	}
}
