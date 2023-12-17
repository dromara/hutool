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

package org.dromara.hutool.extra.management;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * FtpException异常
 *
 * @author Looly
 */
public class ManagementException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ManagementException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ManagementException(final String message) {
		super(message);
	}

	public ManagementException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ManagementException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ManagementException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ManagementException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
