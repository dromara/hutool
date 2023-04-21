/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.exceptions;

/**
 * 带有状态码的异常
 *
 * @author looly
 */
public class StatefulException extends HutoolException {
	private static final long serialVersionUID = 1L;

	// 异常状态码
	private int status;

	/**
	 * 构造
	 */
	public StatefulException() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param e 异常
	 */
	public StatefulException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public StatefulException(final String message) {
		super(message);
	}

	/**
	 * 构造
	 *
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public StatefulException(final String messageTemplate, final Object... params) {
		super(messageTemplate, params);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 * @param cause   被包装的子异常
	 */
	public StatefulException(final String message, final Throwable cause) {
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
	public StatefulException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造
	 *
	 * @param cause           被包装的子异常
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public StatefulException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(cause, messageTemplate, params);
	}

	/**
	 * 构造
	 *
	 * @param status 状态码
	 * @param msg    消息
	 */
	public StatefulException(final int status, final String msg) {
		super(msg);
		this.status = status;
	}

	/**
	 * 构造
	 *
	 * @param status    状态码
	 * @param throwable 异常
	 */
	public StatefulException(final int status, final Throwable throwable) {
		super(throwable);
		this.status = status;
	}

	/**
	 * @param status    状态码
	 * @param msg       消息
	 * @param throwable 异常
	 */
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
