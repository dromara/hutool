package cn.hutool.cron;

import cn.hutool.core.text.StrUtil;

/**
 * 定时任务异常
 *
 * @author xiaoleilu
 */
public class CronException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CronException(final Throwable e) {
		super(e.getMessage(), e);
	}

	public CronException(final String message) {
		super(message);
	}

	public CronException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CronException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public CronException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
