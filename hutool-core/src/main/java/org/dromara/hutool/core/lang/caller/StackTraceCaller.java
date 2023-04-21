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

package org.dromara.hutool.core.lang.caller;

import java.io.Serializable;

import org.dromara.hutool.core.exceptions.HutoolException;

/**
 * 通过StackTrace方式获取调用者。此方式效率最低，不推荐使用
 *
 * @author Looly
 */
public class StackTraceCaller implements Caller, Serializable {
	private static final long serialVersionUID = 1L;
	private static final int OFFSET = 2;

	@Override
	public Class<?> getCaller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (OFFSET + 1 >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + 1].getClassName();
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new HutoolException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCallerCaller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (OFFSET + 2 >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + 2].getClassName();
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new HutoolException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCaller(final int depth) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (OFFSET + depth >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + depth].getClassName();
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new HutoolException(e, "[{}] not found!", className);
		}
	}

	@Override
	public boolean isCalledBy(final Class<?> clazz) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (final StackTraceElement element : stackTrace) {
			if (element.getClassName().equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}
}
