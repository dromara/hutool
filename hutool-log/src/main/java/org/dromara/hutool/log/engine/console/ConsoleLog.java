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

package org.dromara.hutool.log.engine.console;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.AbstractLog;
import org.dromara.hutool.log.level.Level;

/**
 * 利用System.out.println()打印日志
 *
 * @author Looly
 */
public class ConsoleLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private static final String logFormat = "[{date}] [{level}] {name}: {msg}";
	private static Level currentLevel = Level.DEBUG;

	private final String name;

	//------------------------------------------------------------------------- Constructor

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ConsoleLog(final Class<?> clazz) {
		this.name = (null == clazz) ? StrUtil.NULL : clazz.getName();
	}

	/**
	 * 构造
	 *
	 * @param name 类名
	 */
	public ConsoleLog(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 设置自定义的日志显示级别
	 *
	 * @param customLevel 自定义级别
	 * @since 4.1.10
	 */
	public static void setLevel(final Level customLevel) {
		Assert.notNull(customLevel);
		currentLevel = customLevel;
	}

	//------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return isEnabled(Level.TRACE);
	}

	@Override
	public void trace(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.TRACE, t, format, arguments);
	}

	//------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return isEnabled(Level.DEBUG);
	}

	@Override
	public void debug(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.DEBUG, t, format, arguments);
	}

	//------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return isEnabled(Level.INFO);
	}

	@Override
	public void info(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.INFO, t, format, arguments);
	}

	//------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return isEnabled(Level.WARN);
	}

	@Override
	public void warn(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.WARN, t, format, arguments);
	}

	//------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return isEnabled(Level.ERROR);
	}

	@Override
	public void error(final String fqcn, final Throwable t, final String format, final Object... arguments) {
		log(fqcn, Level.ERROR, t, format, arguments);
	}

	//------------------------------------------------------------------------- Log
	@Override
	public void log(final String fqcn, final Level level, final Throwable t, final String format, final Object... arguments) {
		// fqcn 无效
		if (!isEnabled(level)) {
			return;
		}


		final Dict dict = Dict.of()
				.set("date", DateUtil.formatNow())
				.set("level", level.toString())
				.set("name", this.name)
				.set("msg", StrUtil.format(format, arguments));

		final String logMsg = StrUtil.format(logFormat, dict);

		//WARN以上级别打印至System.err
		if (level.ordinal() >= Level.WARN.ordinal()) {
			Console.error(t, logMsg);
		} else {
			Console.log(t, logMsg);
		}
	}

	@Override
	public boolean isEnabled(final Level level) {
		return currentLevel.compareTo(level) <= 0;
	}
}
