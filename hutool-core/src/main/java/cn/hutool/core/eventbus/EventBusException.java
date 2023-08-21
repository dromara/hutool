package cn.hutool.core.eventbus;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * EventBus异常
 *
 * @author unknowIfGuestInDream
 */
public class EventBusException extends RuntimeException {

	public EventBusException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public EventBusException(String message) {
		super(message);
	}

	public EventBusException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public EventBusException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EventBusException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public EventBusException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
