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

package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 资源文件或资源不存在异常
 *
 * @author Looly
 * @since 4.0.2
 */
public class NoResourceException extends IORuntimeException {
	private static final long serialVersionUID = -623254467603299129L;

	/**
	 * 构造
	 */
	public NoResourceException() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param e 异常
	 */
	public NoResourceException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public NoResourceException(final String message) {
		super(message);
	}

	/**
	 * 构造
	 *
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public NoResourceException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 * @param cause   被包装的子异常
	 */
	public NoResourceException(final String message, final Throwable cause) {
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
	public NoResourceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造
	 *
	 * @param cause           被包装的子异常
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public NoResourceException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), cause);
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
