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

package org.dromara.hutool;

import org.dromara.hutool.core.lang.ConsoleTable;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Set;

/**
 * <pre>
 * ========================================
 *     __  __        __                   __
 *    / / / /__  __ / /_ __ __   __ __   / /
 *   / /_/ // / / // __// _ _ \ / _ _ \ / /
 *  / __  // /_/ // /_ / /__/ // /__/ // /
 * /_/ /_/ \____/ \__/ \_____/ \_____//_/
 *
 * -----------https://hutool.cn/-----------
 * ========================================
 * </pre>
 *
 * <p>
 * Hutool是一个功能丰富且易用的Java工具库，通过诸多实用工具类的使用，旨在帮助开发者快速、便捷地完成各类开发任务。
 * 这些封装的工具涵盖了字符串、数字、集合、编码、日期、文件、IO、加密、数据库JDBC、JSON、HTTP客户端等一系列操作，
 * 可以满足各种不同的开发需求。
 * </p>
 *
 * @author Looly
 */
public class Hutool {

	/**
	 * 作者（贡献者）
	 */
	public static final String AUTHOR = "Looly";

	private Hutool() {
	}

	/**
	 * 显示Hutool所有的工具类
	 *
	 * @return 工具类名集合
	 * @since 5.5.2
	 */
	public static Set<Class<?>> getAllUtils() {
		return ClassUtil.scanPackage("org.dromara.hutool",
				(clazz) -> (!clazz.isInterface()) && StrUtil.endWith(clazz.getSimpleName(), "Util"));
	}

	/**
	 * 控制台打印所有工具类
	 */
	public static void printAllUtils() {
		final Set<Class<?>> allUtils = getAllUtils();
		final ConsoleTable consoleTable = ConsoleTable.of().addHeader("工具类名", "所在包");
		for (final Class<?> clazz : allUtils) {
			consoleTable.addBody(clazz.getSimpleName(), clazz.getPackage().getName());
		}
		consoleTable.print();
	}
}
