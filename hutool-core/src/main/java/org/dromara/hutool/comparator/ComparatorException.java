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

package org.dromara.hutool.comparator;

import org.dromara.hutool.exceptions.ExceptionUtil;
import org.dromara.hutool.text.StrUtil;

/**
 * 比较异常
 * @author xiaoleilu
 */
public class ComparatorException extends RuntimeException{
	private static final long serialVersionUID = 4475602435485521971L;

	public ComparatorException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ComparatorException(final String message) {
		super(message);
	}

	public ComparatorException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ComparatorException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ComparatorException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
