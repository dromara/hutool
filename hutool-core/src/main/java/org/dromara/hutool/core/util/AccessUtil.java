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

package org.dromara.hutool.core.util;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * {@link AccessController}相关封装
 *
 * @author looly
 * @since 6.0.0
 */
public class AccessUtil {

	/**
	 * @param action  执行内容
	 * @param context 上下文，当为{@code null}时直接执行内容，而不检查
	 * @param <T>     执行结果类型
	 * @return 结果
	 */
	public static <T> T doPrivileged(final PrivilegedAction<T> action,
									 final AccessControlContext context) {
		if (null == context) {
			return action.run();
		}

		return AccessController.doPrivileged(action, context);
	}
}
