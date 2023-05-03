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

package org.dromara.hutool.core.reflect.kotlin;

import org.dromara.hutool.core.lang.Opt;

import java.util.List;

/**
 * Kotlin反射包装相关工具类
 *
 * @author VampireAchao, Looly
 */
public class KClassUtil {

	/**
	 * 是否提供或处于Kotlin环境中
	 */
	public static final boolean IS_KOTLIN_ENABLE =
		Opt.ofTry(() -> Class.forName("kotlin.Metadata")).isPresent();

	/**
	 * 获取Kotlin类的所有构造方法
	 *
	 * @param targetType kotlin类
	 * @return 构造列表
	 */
	public static List<?> getConstructors(final Class<?> targetType) {
		return KClassImpl.getConstructors(targetType);
	}

	/**
	 * 获取参数列表
	 *
	 * @param kCallable kotlin的类、方法或构造
	 * @return 参数列表
	 */
	public static List<KParameter> getParameters(final Object kCallable) {
		return KCallable.getParameters(kCallable);
	}
}
