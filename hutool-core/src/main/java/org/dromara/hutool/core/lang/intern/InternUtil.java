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

package org.dromara.hutool.core.lang.intern;

/**
 * 规范化对象生成工具
 *
 * @author looly
 * @since 5.4.3
 */
public class InternUtil {

	/**
	 * 创建WeakHshMap实现的字符串规范化器
	 *
	 * @param <T> 规范对象的类型
	 * @return {@link Intern}
	 */
	public static <T> Intern<T> ofWeak(){
		return new WeakIntern<>();
	}

	/**
	 * 创建JDK默认实现的字符串规范化器
	 *
	 * @return {@link Intern}
	 * @see String#intern()
	 */
	public static Intern<String> ofString(){
		return new StringIntern();
	}

	/**
	 * 创建字符串规范化器
	 *
	 * @param isWeak 是否创建使用WeakHashMap实现的Interner
	 * @return {@link Intern}
	 */
	public static Intern<String> of(final boolean isWeak){
		return isWeak ? ofWeak() : ofString();
	}
}
