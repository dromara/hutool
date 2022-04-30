package cn.hutool.json.jwt;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * JWT异常
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JWTException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public JWTException(final String message) {
		super(message);
	}

	public JWTException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public JWTException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public JWTException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public JWTException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
