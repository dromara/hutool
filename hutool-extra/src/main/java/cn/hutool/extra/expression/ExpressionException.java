package cn.hutool.extra.expression;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 表达式语言异常
 *
 * @author Looly
 */
public class ExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExpressionException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ExpressionException(final String message) {
		super(message);
	}

	public ExpressionException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ExpressionException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ExpressionException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ExpressionException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
