package cn.hutool.extra.ftp;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * Ftp异常
 *
 * @author xiaoleilu
 */
public class FtpException extends RuntimeException {
	private static final long serialVersionUID = -8490149159895201756L;

	public FtpException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public FtpException(final String message) {
		super(message);
	}

	public FtpException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public FtpException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public FtpException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public FtpException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
