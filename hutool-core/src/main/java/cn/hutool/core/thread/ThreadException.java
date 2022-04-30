package cn.hutool.core.thread;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 工具类异常
 *
 * @author looly
 * @since 5.7.17
 */
public class ThreadException extends RuntimeException {
	private static final long serialVersionUID = 5253124428623713216L;

	public ThreadException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ThreadException(final String message) {
		super(message);
	}

	public ThreadException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ThreadException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ThreadException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ThreadException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
