package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 依赖异常
 *
 * @author xiaoleilu
 * @since 4.0.10
 */
public class DependencyException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DependencyException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DependencyException(final String message) {
		super(message);
	}

	public DependencyException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DependencyException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public DependencyException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public DependencyException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
