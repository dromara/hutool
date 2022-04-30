package cn.hutool.core.compiler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 编译异常
 *
 * @author looly
 * @since 5.5.2
 */
public class CompilerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CompilerException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CompilerException(final String message) {
		super(message);
	}

	public CompilerException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CompilerException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public CompilerException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
