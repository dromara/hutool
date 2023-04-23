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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * IO运行时异常，常用于对IOException的包装
 *
 * @author xiaoleilu
 */
public class IORuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public IORuntimeException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public IORuntimeException(final String message) {
		super(message);
	}

	public IORuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public IORuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public IORuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}

	/**
	 * 导致这个异常的异常是否是指定类型的异常
	 *
	 * @param clazz 异常类
	 * @return 是否为指定类型异常
	 */
	public boolean causeInstanceOf(final Class<? extends Throwable> clazz) {
		final Throwable cause = this.getCause();
		return null != clazz && clazz.isInstance(cause);
	}
}
