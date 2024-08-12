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

package org.dromara.hutool.core.pool;

/**
 * 对象工厂接口，用于自定义对象创建、验证和销毁<br>
 * 来自：https://github.com/DanielYWoo/fast-object-pool/
 *
 * @param <T> 对象类型
 * @author Daniel
 */
public interface ObjectFactory<T> {
	/**
	 * 创建对象
	 *
	 * @return 创建的对象
	 */
	T create();

	/**
	 * 验证对象可用性，一般用于对象池中借出对象和返还对象前的验证操作。
	 *
	 * @param t 被验证的对象
	 * @return 是否可用
	 */
	boolean validate(T t);

	/**
	 * 销毁对象，用于在验证对象不可用或不需要时的销毁逻辑。
	 *
	 * @param t 被销毁的对象
	 */
	void destroy(T t);
}
