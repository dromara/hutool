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
 * 规范化表示形式封装<br>
 * 所谓规范化，即当两个对象equals时，规范化的对象则可以实现==<br>
 * 此包中的相关封装类似于 {@link String#intern()}
 *
 * @param <T> 规范化的对象类型
 * @author looly
 * @since 5.4.3
 */
public interface Intern<T> {

	/**
	 * 返回指定对象对应的规范化对象，sample对象可能有多个，但是这些对象如果都equals，则返回的是同一个对象
	 *
	 * @param sample 对象
	 * @return 样例对象
	 */
	T intern(T sample);
}
