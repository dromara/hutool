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

package org.dromara.hutool.http.webservice;

import org.dromara.hutool.core.text.StrUtil;

/**
 * SOAP异常
 *
 * @author Looly
 */
public class SoapRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public SoapRuntimeException(final Throwable e) {
		super(e.getMessage(), e);
	}

	public SoapRuntimeException(final String message) {
		super(message);
	}

	public SoapRuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public SoapRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public SoapRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
