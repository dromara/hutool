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

package cn.hutool.log.dialect.tinylog;

import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntryForwarder;
import org.pmw.tinylog.Logger;

import cn.hutool.core.array.ArrayUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://www.tinylog.org/">tinylog</a> log.<br>
 *
 * @author Looly
 */
public class TinyLog extends AbstractLog {
	private static final long serialVersionUID = -4848042277045993735L;

	/**
	 * 堆栈增加层数，因为封装因此多了两层，此值用于正确获取当前类名
	 */
	private static final int DEPTH = 4;

	private final int level;
	private final String name;

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	// ------------------------------------------------------------------------- Constructor
	public TinyLog(final Class<?> clazz) {
		this(null == clazz ? StrUtil.NULL : clazz.getName());
	}

	/**
	 * 构造
	 *
	 * @param name 日志名
	 */
	public TinyLog(final String name) {
		this.name = name;
		this.level = Logger.getLevel(name).ordinal();
	}

	@Override
	public String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return this.level <= Level.TRACE.ordinal();
	}

	@Override
	public void trace(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return this.level <= Level.DEBUG.ordinal();
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.DEBUG, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return this.level <= Level.INFO.ordinal();
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return this.level <= org.pmw.tinylog.Level.WARNING.ordinal();
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.WARNING, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return this.level <= Level.ERROR.ordinal();
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final cn.hutool.log.level.Level level, final Throwable t, final String format, final Object... arguments) {
		logIfEnabled(fqcn, toTinyLevel(level), t, format, arguments);
	}

	@Override
	public boolean isEnabled(final cn.hutool.log.level.Level level) {
		return this.level <= toTinyLevel(level).ordinal();
	}

	/**
	 * 在对应日志级别打开情况下打印日志
	 *
	 * @param fqcn      完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param level     日志级别
	 * @param t         异常，null则检查最后一个参数是否为Throwable类型，是则取之，否则不打印堆栈
	 * @param format    日志消息模板
	 * @param arguments 日志消息参数
	 */
	private void logIfEnabled(final String fqcn, final Level level, Throwable t, final String format, final Object... arguments) {
		// fqcn 无效
		if (null == t) {
			t = getLastArgumentIfThrowable(arguments);
		}
		LogEntryForwarder.forward(DEPTH, level, t, StrUtil.toString(format), arguments);
	}

	/**
	 * 将Hutool的Level等级转换为Tinylog的Level等级
	 *
	 * @param level Hutool的Level等级
	 * @return Tinylog的Level
	 * @since 4.0.3
	 */
	private Level toTinyLevel(final cn.hutool.log.level.Level level) {
		final Level tinyLevel;
		switch (level) {
			case TRACE:
				tinyLevel = Level.TRACE;
				break;
			case DEBUG:
				tinyLevel = Level.DEBUG;
				break;
			case INFO:
				tinyLevel = Level.INFO;
				break;
			case WARN:
				tinyLevel = Level.WARNING;
				break;
			case ERROR:
				tinyLevel = Level.ERROR;
				break;
			case OFF:
				tinyLevel = Level.OFF;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		return tinyLevel;
	}

	/**
	 * 如果最后一个参数为异常参数，则获取之，否则返回null
	 *
	 * @param arguments 参数
	 * @return 最后一个异常参数
	 * @since 4.0.3
	 */
	private static Throwable getLastArgumentIfThrowable(final Object... arguments) {
		if (ArrayUtil.isNotEmpty(arguments) && arguments[arguments.length - 1] instanceof Throwable) {
			return (Throwable) arguments[arguments.length - 1];
		} else {
			return null;
		}
	}
}
