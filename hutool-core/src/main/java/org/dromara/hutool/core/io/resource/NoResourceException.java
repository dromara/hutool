/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
