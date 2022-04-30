package cn.hutool.core.io.resource;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.text.StrUtil;

/**
 * 资源文件或资源不存在异常
 *
 * @author xiaoleilu
 * @since 4.0.2
 */
public class NoResourceException extends IORuntimeException {
	private static final long serialVersionUID = -623254467603299129L;

	public NoResourceException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public NoResourceException(final String message) {
		super(message);
	}

	public NoResourceException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public NoResourceException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public NoResourceException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}

	/**
	 * 导致这个异常的异常是否是指定类型的异常
	 *
	 * @param clazz 异常类
	 * @return 是否为指定类型异常
	 */
	@Override
	public boolean causeInstanceOf(final Class<? extends Throwable> clazz) {
		final Throwable cause = this.getCause();
		return clazz.isInstance(cause);
	}
}
