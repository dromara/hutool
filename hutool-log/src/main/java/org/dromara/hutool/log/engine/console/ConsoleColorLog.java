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
import org.dromara.hutool.core.lang.ansi.Ansi4BitColor;
import org.dromara.hutool.core.lang.ansi.AnsiEncoder;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.level.Level;

import java.util.function.Function;

/**
 * 利用System.out.println()打印彩色日志
 *
 * @author hongda.li, looly
 * @since 5.8.0
 */
public class ConsoleColorLog extends ConsoleLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 控制台打印类名的颜色代码
	 */
	private static final Ansi4BitColor COLOR_CLASSNAME = Ansi4BitColor.CYAN;

	/**
	 * 控制台打印时间的颜色代码
	 */
	private static final Ansi4BitColor COLOR_TIME = Ansi4BitColor.WHITE;

	/**
	 * 控制台打印正常信息的颜色代码
	 */
	private static final Ansi4BitColor COLOR_NONE = Ansi4BitColor.DEFAULT;

	private static Function<Level, Ansi4BitColor> colorFactory = (level -> {
		switch (level) {
			case DEBUG:
			case INFO:
				return Ansi4BitColor.GREEN;
			case WARN:
				return Ansi4BitColor.YELLOW;
			case ERROR:
				return Ansi4BitColor.RED;
			case TRACE:
				return Ansi4BitColor.MAGENTA;
			default:
				return COLOR_NONE;
		}
	});

	/**
	 * 设置颜色工厂，根据日志级别，定义不同的颜色
	 *
	 * @param colorFactory 颜色工厂函数
	 */
	public static void setColorFactory(final Function<Level, Ansi4BitColor> colorFactory) {
		ConsoleColorLog.colorFactory = colorFactory;
	}

	/**
	 * 构造
	 *
	 * @param name 类名
	 */
	public ConsoleColorLog(final String name) {
		super(name);
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ConsoleColorLog(final Class<?> clazz) {
		super(clazz);
	}

	@Override
	public synchronized void log(final String fqcn, final Level level, final Throwable t, final String format, final Object... arguments) {
		if (! isEnabled(level)) {
			return;
		}

		final String template = AnsiEncoder.encode(COLOR_TIME, "[%s]", colorFactory.apply(level), "[%-5s]%s", COLOR_CLASSNAME, "%-30s: ", COLOR_NONE, "%s%n");
		System.out.format(template, DateUtil.formatNow(), level.name(), " - ", ClassUtil.getShortClassName(getName()), StrUtil.format(format, arguments));
	}
}
