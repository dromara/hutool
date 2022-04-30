package cn.hutool.extra.compress;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 压缩解压异常语言异常
 *
 * @author Looly
 */
public class CompressException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CompressException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CompressException(final String message) {
		super(message);
	}

	public CompressException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CompressException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public CompressException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public CompressException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
