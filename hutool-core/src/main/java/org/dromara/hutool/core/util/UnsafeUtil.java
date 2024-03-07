/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.reflect.FieldUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * {@link Unsafe}对象获取工具<br>
 * 参考：fastjson2/util/JDKUtils.java<br>
 * 此工具类通过反射获取，绕开安全检查。
 *
 * @author Looly
 * @since 6.0.0
 */
public class UnsafeUtil {
	private static final Unsafe UNSAFE;

	static {
		final String fieldName = JdkUtil.IS_ANDROID ? "THE_ONE" : "theUnsafe";
		final Field theUnsafeField = FieldUtil.getField(Unsafe.class, fieldName);
		UNSAFE = (Unsafe) FieldUtil.getStaticFieldValue(theUnsafeField);
	}

	/**
	 * 获取{@link Unsafe}对象
	 *
	 * @return {@link Unsafe}
	 */
	public static Unsafe getUnsafe() {
		return UNSAFE;
	}
}
