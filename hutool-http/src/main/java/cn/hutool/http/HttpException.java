package cn.hutool.http;

import cn.hutool.core.text.StrUtil;

/**
 * HTTP异常
 *
 * @author xiaoleilu
 */
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public HttpException(final Throwable e) {
		super(e.getMessage(), e);
	}

	public HttpException(final String message) {
		super(message);
	}

	public HttpException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public HttpException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public HttpException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public HttpException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
