package cn.hutool.db;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 数据库异常
 * @author xiaoleilu
 */
public class DbRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 3624487785708765623L;

	public DbRuntimeException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DbRuntimeException(final String message) {
		super(message);
	}

	public DbRuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DbRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public DbRuntimeException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public DbRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
