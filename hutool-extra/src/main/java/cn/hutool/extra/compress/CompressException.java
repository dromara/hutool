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

package cn.hutool.extra.compress;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 压缩解压异常语言异常
 *
 * @author Looly
 */
public class CompressException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CompressException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CompressException(final String message) {
		super(message);
	}

	public CompressException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CompressException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public CompressException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public CompressException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
