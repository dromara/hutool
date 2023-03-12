package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 工具类异常
 *
 * @author xiaoleilu
 */
public class UtilException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(final Throwable cause) {
		super(ExceptionUtil.getMessage(cause), cause);
	}

	public UtilException(final String message) {
		super(message);
	}

	public UtilException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public UtilException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UtilException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtilException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), cause);
	}
}
