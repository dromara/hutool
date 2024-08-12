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
