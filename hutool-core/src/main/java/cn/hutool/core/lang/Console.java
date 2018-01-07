package cn.hutool.core.lang;

import static java.lang.System.out;

import java.util.Scanner;

import cn.hutool.core.util.StrUtil;

import static java.lang.System.err;

/**
 * 命令行（控制台）工具方法类<br>
 * 此类主要针对{@link System#out} 和 {@link System#err} 做封装。
 * 
 * @author Looly
 *
 */

public class Console {

	// --------------------------------------------------------------------------------- Log
	/**
	 * 同 System.out.println()方法，打印控制台日志
	 */
	public static void log() {
		out.println();
	}

	/**
	 * 同 System.out.println()方法，打印控制台日志<br>
	 * 如果传入打印对象为{@link Throwable}对象，那么同时打印堆栈
	 * 
	 * @param obj 要打印的对象
	 */
	public static void log(Object obj) {
		if (obj instanceof Throwable) {
			Throwable e = (Throwable) obj;
			log(e, e.getMessage());
		} else {
			log("{}", obj);
		}
	}

	/**
	 * 同 System.out.print()方法，打印控制台日志
	 * 
	 * @param obj 要打印的对象
	 * @since 3.3.1
	 */
	public static void print(Object obj) {
		print("{}", obj);
	}

	/**
	 * 同 System.out.println()方法，打印控制台日志
	 * 
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void log(String template, Object... values) {
		log(null, template, values);
	}

	/**
	 * 同 System.out.print()方法，打印控制台日志
	 * 
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 * @since 3.3.1
	 */
	public static void print(String template, Object... values) {
		out.print(StrUtil.format(template, values));
	}

	/**
	 * 同 System.out.println()方法，打印控制台日志
	 * 
	 * @param t 异常对象
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void log(Throwable t, String template, Object... values) {
		out.println(StrUtil.format(template, values));
		if (null != t) {
			t.printStackTrace();
			out.flush();
		}
	}

	// --------------------------------------------------------------------------------- Error
	/**
	 * 同 System.err.println()方法，打印控制台日志
	 */
	public static void error() {
		err.println();
	}

	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * 
	 * @param obj 要打印的对象
	 */
	public static void error(Object obj) {
		if (obj instanceof Throwable) {
			Throwable e = (Throwable) obj;
			error(e, e.getMessage());
		} else {
			error("{}", obj);
		}
	}

	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * 
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void error(String template, Object... values) {
		error(null, template, values);
	}

	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * 
	 * @param t 异常对象
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void error(Throwable t, String template, Object... values) {
		err.println(StrUtil.format(template, values));
		if (null != t) {
			t.printStackTrace(err);
			err.flush();
		}
	}

	// --------------------------------------------------------------------------------- in
	/**
	 * 创建从控制台读取内容的{@link Scanner}
	 * 
	 * @return {@link Scanner}
	 * @since 3.3.1
	 */
	public static Scanner scanner() {
		return new Scanner(System.in);
	}

	/**
	 * 读取用户输入的内容（在控制台敲回车前的内容）
	 * 
	 * @return 用户输入的内容
	 * @since 3.3.1
	 */
	public static String input() {
		return scanner().next();
	}
}
