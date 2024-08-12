/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 * 强引用，不回收
	 */
	STRONG,
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
