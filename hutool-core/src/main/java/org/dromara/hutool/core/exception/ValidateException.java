/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.exception;

/**
 * 验证异常
 *
 * @author Looly
 */
public class ValidateException extends StatefulException {
	private static final long serialVersionUID = 6057602589533840889L;

	/**
	 * 构造
	 */
	public ValidateException() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param e 异常
	 */
	public ValidateException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public ValidateException(final String message) {
		super(message);
	}

	/**
	 * 构造
	 *
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public ValidateException(final String messageTemplate, final Object... params) {
		super(messageTemplate, params);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 * @param cause   被包装的子异常
	 */
	public ValidateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造
	 *
	 * @param message            消息
	 * @param cause              被包装的子异常
	 * @param enableSuppression  是否启用抑制
	 * @param writableStackTrace 堆栈跟踪是否应该是可写的
	 */
	public ValidateException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造
	 *
	 * @param cause           被包装的子异常
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public ValidateException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(cause, messageTemplate, params);
	}

	/**
	 * 构造
	 *
	 * @param status 状态码
	 * @param msg    消息
	 */
	public ValidateException(final int status, final String msg) {
		super(status, msg);
	}

	/**
	 * 构造
	 *
	 * @param status    状态码
	 * @param throwable 异常
	 */
	public ValidateException(final int status, final Throwable throwable) {
		super(status, throwable);
	}

	/**
	 * @param status    状态码
	 * @param msg       消息
	 * @param throwable 异常
	 */
	public ValidateException(final int status, final String msg, final Throwable throwable) {
		super(status, msg, throwable);
	}
}
