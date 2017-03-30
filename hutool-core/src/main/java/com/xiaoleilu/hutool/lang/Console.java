package com.xiaoleilu.hutool.lang;

import com.xiaoleilu.hutool.util.StrUtil;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * 命令行工具方法类
 * @author Looly
 *
 */
public final class Console {
	
	private Console() {}
	
	//--------------------------------------------------------------------------------- Log
	/**
	 * 同 System.out.println()方法，打印控制台日志
	 */
	public static void log(){
		out.println();
	}
	
	/**
	 * 同 System.out.println()方法，打印控制台日志
	 * @param obj 要打印的对象
	 */
	public static void log(Object obj){
		if(obj instanceof Throwable){
			Throwable e = (Throwable)obj;
			log(e, e.getMessage());
		}else{
			log("{}", obj);
		}
	}
	
	/**
	 * 同 System.out.println()方法，打印控制台日志
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void log(String template, Object... values){
		log(null, template, values);
	}
	
	/**
	 * 同 System.out.println()方法，打印控制台日志
	 * @param t 异常对象
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void log(Throwable t, String template, Object... values){
		out.println(StrUtil.format(template, values));
		if(null != t){
			t.printStackTrace();
			out.flush();
		}
	}

	//--------------------------------------------------------------------------------- Error
	/**
	 * 同 System.err.println()方法，打印控制台日志
	 */
	public static void error(){
		err.println();
	}
	
	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * @param obj 要打印的对象
	 */
	public static void error(Object obj){
		if(obj instanceof Throwable){
			Throwable e = (Throwable)obj;
			error(e, e.getMessage());
		}else{
			error("{}", obj);
		}
	}
	
	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void error(String template, Object... values){
		error(null, template, values);
	}
	
	/**
	 * 同 System.err.println()方法，打印控制台日志
	 * @param t 异常对象
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 值
	 */
	public static void error(Throwable t, String template, Object... values){
		err.println(StrUtil.format(template, values));
		if(null != t){
			t.printStackTrace(err);
			err.flush();
		}
	}
}
