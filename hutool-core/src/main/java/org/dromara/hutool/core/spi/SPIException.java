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

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.exceptions.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * SPI异常
 *
 * @author looly
 */
public class SPIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SPIException(final Throwable cause) {
		super(ExceptionUtil.getMessage(cause), cause);
	}

	public SPIException(final String message) {
		super(message);
	}

	public SPIException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public SPIException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SPIException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SPIException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), cause);
	}
}
