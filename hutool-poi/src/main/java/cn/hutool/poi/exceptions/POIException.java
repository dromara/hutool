package cn.hutool.poi.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * POI异常
 *
 * @author xiaoleilu
 */
public class POIException extends RuntimeException {
	private static final long serialVersionUID = 2711633732613506552L;

	public POIException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public POIException(final String message) {
		super(message);
	}

	public POIException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public POIException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public POIException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public POIException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
