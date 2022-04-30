package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 验证异常
 *
 * @author xiaoleilu
 */
public class ValidateException extends StatefulException {
	private static final long serialVersionUID = 6057602589533840889L;

	public ValidateException() {
	}

	public ValidateException(final String msg) {
		super(msg);
	}

	public ValidateException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ValidateException(final Throwable throwable) {
		super(throwable);
	}

	public ValidateException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}

	public ValidateException(final int status, final String msg) {
		super(status, msg);
	}

	public ValidateException(final int status, final Throwable throwable) {
		super(status, throwable);
	}

	public ValidateException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ValidateException(final int status, final String msg, final Throwable throwable) {
		super(status, msg, throwable);
	}
}
