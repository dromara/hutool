package com.xiaoleilu.hutool.exceptions;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import com.xiaoleilu.hutool.io.FastByteArrayOutputStream;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 异常工具类
 * 
 * @author Looly
 *
 */
public class ExceptionUtil {

	/**
	 * 获得完整消息，包括异常名
	 * 
	 * @param e 异常
	 * @return 完整消息
	 */
	public static String getMessage(Throwable e) {
		return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
	}

	/**
	 * 剥离反射引发的InvocationTargetException、UndeclaredThrowableException中间异常，返回业务本身的异常
	 * @param wrapped 包装的异常
	 * @return 剥离后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}
	
	/**
	 * 堆栈转为完整字符串
	 * @param throwable 异常对象
	 * @return 堆栈转为的字符串
	 */
	public static String stacktraceToString(Throwable throwable, int limit){
		final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
		throwable.printStackTrace(new PrintStream(baos));
		String exceptionStr = baos.toString();
		int length = exceptionStr.length();
		if(limit > 0 && limit < length){
			length = limit;
		}
		
		final StringBuilder sb = StrUtil.builder();
		char c;
		for(int i = 0; i < length; i++){
			c = exceptionStr.charAt(i);
			switch (c) {
				case '\r':
					break;
				case '\n':
					sb.append(' ');
					break;
				case '	':
					sb.append(' ');
					break;
				default:
					sb.append(c);
					break;
			}
		}
		
		return sb.toString();
	}
}
