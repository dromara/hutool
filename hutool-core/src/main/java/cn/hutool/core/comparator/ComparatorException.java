package cn.hutool.core.comparator;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 比较异常
 * @author xiaoleilu
 */
public class ComparatorException extends RuntimeException{
	private static final long serialVersionUID = 4475602435485521971L;

	public ComparatorException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ComparatorException(final String message) {
		super(message);
	}

	public ComparatorException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ComparatorException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ComparatorException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
