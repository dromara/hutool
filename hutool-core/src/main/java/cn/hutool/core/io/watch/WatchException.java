package cn.hutool.core.io.watch;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 监听异常
 * @author Looly
 *
 */
public class WatchException extends RuntimeException {
	private static final long serialVersionUID = 8068509879445395353L;

	public WatchException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public WatchException(final String message) {
		super(message);
	}

	public WatchException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public WatchException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public WatchException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
