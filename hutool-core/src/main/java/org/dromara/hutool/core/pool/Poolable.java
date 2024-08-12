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

import org.dromara.hutool.core.lang.wrapper.Wrapper;

/**
 * 池化对象
 *
 * @param <T> 对象类型
 * @author Looly
 */
public interface Poolable<T> extends Wrapper<T> {

	/**
	 * 获取最后借出时间
	 *
	 * @return 最后借出时间
	 */
	long getLastBorrow();

	/**
	 * 设置最后借出时间，在成功借出此对象时更新时间
	 *
	 * @param lastBorrow 最后借出时间
	 */
	void setLastBorrow(final long lastBorrow);
}
