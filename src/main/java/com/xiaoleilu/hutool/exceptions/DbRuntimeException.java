package com.xiaoleilu.hutool.exceptions;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 数据库异常
 * @author xiaoleilu
 */
public class DbRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 3624487785708765623L;

	public DbRuntimeException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public DbRuntimeException(String message) {
		super(message);
	}
	
	public DbRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public DbRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public DbRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
