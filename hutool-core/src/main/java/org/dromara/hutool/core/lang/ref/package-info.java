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

/**
 * 引用工具封装，主要针对{@link java.lang.ref.Reference} 工具化封装<br>
 * 主要封装包括：
 * <pre>
 * 1. {@link java.lang.ref.SoftReference} 软引用，在GC报告内存不足时会被GC回收
 * 2. {@link java.lang.ref.WeakReference} 弱引用，在GC时发现弱引用会回收其对象
 * 3. {@link java.lang.ref.PhantomReference} 虚引用，在GC时发现虚引用对象，会将{@link java.lang.ref.PhantomReference}插入{@link java.lang.ref.ReferenceQueue}。 此时对象未被真正回收，要等到{@link java.lang.ref.ReferenceQueue}被真正处理后才会被回收。
 * </pre>
 */
package org.dromara.hutool.core.lang.ref;
