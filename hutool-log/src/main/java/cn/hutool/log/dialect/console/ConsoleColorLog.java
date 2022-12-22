package cn.hutool.log.dialect.console;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.level.Level;

import java.util.function.Function;

/**
 * 利用System.out.println()打印彩色日志
 *
 * @author hongda.li, looly
 * @since 5.8.0
 */
public class ConsoleColorLog extends ConsoleLog {

	/**
	 * 控制台打印类名的颜色代码
	 */
	private static final AnsiColor COLOR_CLASSNAME = AnsiColor.CYAN;

	/**
	 * 控制台打印时间的颜色代码
	 */
	private static final AnsiColor COLOR_TIME = AnsiColor.WHITE;

	/**
	 * 控制台打印正常信息的颜色代码
	 */
	private static final AnsiColor COLOR_NONE = AnsiColor.DEFAULT;

	private static Function<Level, AnsiColor> colorFactory = (level -> {
		switch (level) {
			case DEBUG:
			case INFO:
				return AnsiColor.GREEN;
			case WARN:
				return AnsiColor.YELLOW;
			case ERROR:
				return AnsiColor.RED;
			case TRACE:
				return AnsiColor.MAGENTA;
			default:
				return COLOR_NONE;
		}
	});

	/**
	 * 设置颜色工厂，根据日志级别，定义不同的颜色
	 *
	 * @param colorFactory 颜色工厂函数
	 */
	public static void setColorFactory(Function<Level, AnsiColor> colorFactory) {
		ConsoleColorLog.colorFactory = colorFactory;
	}

	/**
	 * 构造
	 *
	 * @param name 类名
	 */
	public ConsoleColorLog(String name) {
		super(name);
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ConsoleColorLog(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public synchronized void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		if (false == isEnabled(level)) {
			return;
		}

		final String template = AnsiEncoder.encode(COLOR_TIME, "[%s]", colorFactory.apply(level), "[%-5s]%s", COLOR_CLASSNAME, "%-30s: ", COLOR_NONE, "%s%n");
		System.out.format(template, DateUtil.now(), level.name(), " - ", ClassUtil.getShortClassName(getName()), StrUtil.format(format, arguments));
	}
}
