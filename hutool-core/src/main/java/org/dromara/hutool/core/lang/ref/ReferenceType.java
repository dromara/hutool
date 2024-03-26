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

package org.dromara.hutool.core.lang.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 引用类型
 *
 * @author looly
 */
public enum ReferenceType {
	/**
	 * 软引用，在GC报告内存不足时会被GC回收
	 */
	SOFT,
	/**
	 * 弱引用，在GC时发现弱引用会回收其对象
	 */
	WEAK,
	/**
	 * 虚引用，在GC时发现虚引用对象，会将{@link PhantomReference}插入{@link ReferenceQueue}。 <br>
	 * 此时对象未被真正回收，要等到{@link ReferenceQueue}被真正处理后才会被回收。
	 */
	PHANTOM
}
