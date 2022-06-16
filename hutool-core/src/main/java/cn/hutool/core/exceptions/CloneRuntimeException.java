package cn.hutool.core.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 克隆异常
 * @author xiaoleilu
 */
public class CloneRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 6774837422188798989L;

	public CloneRuntimeException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CloneRuntimeException(final String message) {
		super(message);
	}

	public CloneRuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CloneRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public CloneRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
