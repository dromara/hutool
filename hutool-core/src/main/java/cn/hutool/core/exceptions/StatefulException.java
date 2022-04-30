package cn.hutool.core.exceptions;

import cn.hutool.core.text.StrUtil;

/**
 * 带有状态码的异常
 *
 * @author xiaoleilu
 */
public class StatefulException extends RuntimeException {
	private static final long serialVersionUID = 6057602589533840889L;

	// 异常状态码
	private int status;

	public StatefulException() {
	}

	public StatefulException(final String msg) {
		super(msg);
	}

	public StatefulException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public StatefulException(final Throwable throwable) {
		super(throwable);
	}

	public StatefulException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}

	public StatefulException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public StatefulException(final int status, final String msg) {
		super(msg);
		this.status = status;
	}

	public StatefulException(final int status, final Throwable throwable) {
		super(throwable);
		this.status = status;
	}

	public StatefulException(final int status, final String msg, final Throwable throwable) {
		super(msg, throwable);
		this.status = status;
	}

	/**
	 * @return 获得异常状态码
	 */
	public int getStatus() {
		return status;
	}
}
