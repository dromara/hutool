package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 工具类异常
 *
 * @author xiaoleilu
 */
public class UtilException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public UtilException(final String message) {
		super(message);
	}

	public UtilException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public UtilException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public UtilException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public UtilException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
