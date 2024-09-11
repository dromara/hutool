/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.lang.Assert;

import java.lang.reflect.Member;

/**
 * 类成员，用于获取类的修饰符等，如：
 * <pre>
 *     ClassMember member = new ClassMember(String.class);
 *     Console.log(member.getModifiers());
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class ClassMember implements Member {

	/**
	 * 静态工厂方法，用于创建ClassMember对象
	 *
	 * @param clazz 类
	 * @return ClassMember对象
	 */
	public static ClassMember of(final Class<?> clazz) {
		return new ClassMember(clazz);
	}

	private final Class<?> clazz;

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ClassMember(final Class<?> clazz) {
		this.clazz = Assert.notNull(clazz);
	}

	@Override
	public Class<?> getDeclaringClass() {
		return this.clazz;
	}

	@Override
	public String getName() {
		return this.clazz.getName();
	}

	@Override
	public int getModifiers() {
		return this.clazz.getModifiers();
	}

	@Override
	public boolean isSynthetic() {
		return this.clazz.isSynthetic();
	}
}
