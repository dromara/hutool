package cn.hutool.extra.management;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * FtpException异常
 *
 * @author xiaoleilu
 */
public class ManagementException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ManagementException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ManagementException(final String message) {
		super(message);
	}

	public ManagementException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ManagementException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ManagementException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ManagementException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
