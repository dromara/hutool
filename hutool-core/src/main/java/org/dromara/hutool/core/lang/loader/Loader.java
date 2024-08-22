/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.lang.loader;

/**
 * 对象加载抽象接口<br>
 * 通过实现此接口自定义实现对象的加载方式，例如懒加载机制、多线程加载等
 *
 * @author looly
 *
 * @param <T> 对象类型
 */
@FunctionalInterface
public interface Loader<T> {

	/**
	 * 获取一个准备好的对象<br>
	 * 通过准备逻辑准备好被加载的对象，然后返回。在准备完毕之前此方法应该被阻塞
	 *
	 * @return 加载完毕的对象
	 */
	T get();
}
