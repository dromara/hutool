package cn.hutool.log;

import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.log.level.DebugLog;
import cn.hutool.log.level.ErrorLog;
import cn.hutool.log.level.InfoLog;
import cn.hutool.log.level.Level;
import cn.hutool.log.level.TraceLog;
import cn.hutool.log.level.WarnLog;

/**
 * 日志统一接口
 * 
 * @author Looly
 *
 */
public interface Log extends TraceLog, DebugLog, InfoLog, WarnLog, ErrorLog {

	//------------------------------------------------------------------------ Static method start
	/**
	 * 获得Log
	 *
	 * @param clazz 日志发出的类
	 * @return Log
	 */
	static Log get(Class<?> clazz) {
		return LogFactory.get(clazz);
	}

	/**
	 * 获得Log
	 *
	 * @param name 自定义的日志发出者名称
	 * @return Log
	 * @since 5.0.0
	 */
	static Log get(String name) {
		return LogFactory.get(name);
	}

	/**
	 * @return 获得日志，自动判定日志发出者
	 * @since 5.0.0
	 */
	static Log get() {
		return LogFactory.get(CallerUtil.getCallerCaller());
	}
	//------------------------------------------------------------------------ Static method end

	/**
	 * @return 日志对象的Name
	 */
	String getName();

	/**
	 * 是否开启指定日志
	 * @param level 日志级别
	 * @return 是否开启指定级别
	 */
	boolean isEnabled(Level level);

	/**
	 * 打印指定级别的日志
	 * @param level 级别
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(Level level, String format, Object... arguments);

	/**
	 * 打印 指定级别的日志
	 *
	 * @param level 级别
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(Level level, Throwable t, String format, Object... arguments);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param level 级别
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void log(String fqcn, Level level, Throwable t, String format, Object... arguments);
}
