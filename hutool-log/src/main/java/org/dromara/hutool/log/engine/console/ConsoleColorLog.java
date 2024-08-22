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
		if (!isEnabled(level)) {
			return;
		}

		final String template = AnsiEncoder.encode(COLOR_TIME, "[%s]", colorFactory.apply(level), "[%-5s]%s", COLOR_CLASSNAME, "%-30s: ", COLOR_NONE, "%s%n");
		System.out.format(template, DateUtil.formatNow(), level.name(), " - ", ClassUtil.getShortClassName(getName()), StrUtil.format(format, arguments));
	}
}
