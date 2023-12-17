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

package org.dromara.hutool.http;

import org.dromara.hutool.core.text.StrUtil;

/**
 * HTTP异常
 *
 * @author Looly
 */
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public HttpException(final Throwable e) {
		super(e.getMessage(), e);
	}

	public HttpException(final String message) {
		super(message);
	}

	public HttpException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public HttpException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public HttpException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public HttpException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
