package cn.hutool.core.date;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 工具类异常
 * @author xiaoleilu
 */
public class DateException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public DateException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DateException(final String message) {
		super(message);
	}

	public DateException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DateException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public DateException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
