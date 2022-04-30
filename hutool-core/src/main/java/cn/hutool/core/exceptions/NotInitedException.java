package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 未初始化异常
 *
 * @author xiaoleilu
 */
public class NotInitedException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public NotInitedException(final Throwable e) {
		super(e);
	}

	public NotInitedException(final String message) {
		super(message);
	}

	public NotInitedException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public NotInitedException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public NotInitedException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public NotInitedException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
