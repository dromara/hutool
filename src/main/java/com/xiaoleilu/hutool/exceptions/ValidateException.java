package com.xiaoleilu.hutool.exceptions;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 设置异常
 * @author xiaoleilu
 */
public class ValidateException extends Exception{
	private static final long serialVersionUID = 7295625949787710179L;

	public ValidateException(Throwable e) {
		super(e);
	}
	
	public ValidateException(String message) {
		super(message);
	}
	
	public ValidateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public ValidateException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ValidateException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
