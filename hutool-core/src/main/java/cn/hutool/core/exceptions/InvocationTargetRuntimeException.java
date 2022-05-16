package cn.hutool.core.exceptions;

/**
 * InvocationTargetException的运行时异常
 *
 * @author looly
 */
public class InvocationTargetRuntimeException extends UtilException {

	public InvocationTargetRuntimeException(final Throwable e) {
		super(e);
	}

	public InvocationTargetRuntimeException(final String message) {
		super(message);
	}

	public InvocationTargetRuntimeException(final String messageTemplate, final Object... params) {
		super(messageTemplate, params);
	}

	public InvocationTargetRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public InvocationTargetRuntimeException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public InvocationTargetRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(throwable, messageTemplate, params);
	}
}
