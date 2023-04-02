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

package org.dromara.hutool;

import org.dromara.hutool.lang.caller.CallerUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.level.Level;

/**
 * 静态日志类，用于在不引入日志对象的情况下打印日志
 *
 * @author Looly
 *
 */
public final class StaticLog {
	private static final String FQCN = StaticLog.class.getName();

	private StaticLog() {
	}

	// ----------------------------------------------------------- Log method start
	// ------------------------ Trace
	/**
	 * Trace等级日志，小于debug<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void trace(final String format, final Object... arguments) {
		trace(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Trace等级日志，小于Debug
	 *
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void trace(final Log log, final String format, final Object... arguments) {
		log.trace(FQCN, null, format, arguments);
	}

	// ------------------------ debug
	/**
	 * Debug等级日志，小于Info<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void debug(final String format, final Object... arguments) {
		debug(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Debug等级日志，小于Info
	 *
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void debug(final Log log, final String format, final Object... arguments) {
		log.debug(FQCN, null, format, arguments);
	}

	// ------------------------ info
	/**
	 * Info等级日志，小于Warn<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void info(final String format, final Object... arguments) {
		info(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Info等级日志，小于Warn
	 *
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void info(final Log log, final String format, final Object... arguments) {
		log.info(FQCN, null, format, arguments);
	}

	// ------------------------ warn
	/**
	 * Warn等级日志，小于Error<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(final String format, final Object... arguments) {
		warn(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Warn等级日志，小于Error<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(final Throwable e, final String format, final Object... arguments) {
		warn(LogFactory.get(CallerUtil.getCallerCaller()), e, StrUtil.format(format, arguments));
	}

	/**
	 * Warn等级日志，小于Error
	 *
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(final Log log, final String format, final Object... arguments) {
		warn(log, null, format, arguments);
	}

	/**
	 * Warn等级日志，小于Error
	 *
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(final Log log, final Throwable e, final String format, final Object... arguments) {
		log.warn(FQCN, e, format, arguments);
	}

	// ------------------------ error
	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(final Throwable e) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), e);
	}

	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(final String format, final Object... arguments) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 *
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(final Throwable e, final String format, final Object... arguments) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), e, format, arguments);
	}

	/**
	 * Error等级日志<br>
	 *
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(final Log log, final Throwable e) {
		error(log, e, e.getMessage());
	}

	/**
	 * Error等级日志<br>
	 *
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(final Log log, final String format, final Object... arguments) {
		error(log, null, format, arguments);
	}

	/**
	 * Error等级日志<br>
	 *
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(final Log log, final Throwable e, final String format, final Object... arguments) {
		log.error(FQCN, e, format, arguments);
	}

	// ------------------------ Log
	/**
	 * 打印日志<br>
	 *
	 * @param level 日志级别
	 * @param t 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void log(final Level level, final Throwable t, final String format, final Object... arguments) {
		LogFactory.get(CallerUtil.getCallerCaller()).log(FQCN, level, t, format, arguments);
	}

	// ----------------------------------------------------------- Log method end
}
