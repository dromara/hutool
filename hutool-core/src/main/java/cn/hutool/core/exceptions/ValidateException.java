package cn.hutool.core.exceptions;

import cn.hutool.core.util.StrUtil;

/**
 * 验证异常
 *
 * @author xiaoleilu
 */
public class ValidateException extends StatefulException {
	private static final long serialVersionUID = 6057602589533840889L;

	public ValidateException() {
	}

	public ValidateException(String msg) {
		super(msg);
	}

	public ValidateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ValidateException(Throwable throwable) {
		super(throwable);
	}

	public ValidateException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public ValidateException(int status, String msg) {
		super(status, msg);
	}

	public ValidateException(int status, Throwable throwable) {
		super(status, throwable);
	}

	public ValidateException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ValidateException(int status, String msg, Throwable throwable) {
		super(status, msg, throwable);
	}

	/**
	 * 满足条件就抛出异常
	 *
	 * @param condition 条件
	 * @param msg       异常消息
	 */
	public static void matchThrow(boolean condition, String msg) {
		if (condition) {
			throw new ValidateException(msg);
		}
	}

	/**
	 * 不满足条件就抛出异常
	 *
	 * @param condition 条件
	 * @param msg       异常消息
	 */
	public static void nonMatchThrow(boolean condition, String msg) {
		if (false == condition) {
			throw new ValidateException(msg);
		}
	}

}
