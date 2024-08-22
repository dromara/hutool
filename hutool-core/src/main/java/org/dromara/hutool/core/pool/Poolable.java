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
	 * 获取对象空闲时长，单位：毫秒<br>
	 * 空闲时间指在池中的时间，即借出时间到上次归还时间的差
	 *
	 * @return 空闲毫秒数
	 */
	default long getIdle() {
		return System.currentTimeMillis() - getLastReturn();
	}

	/**
	 * 获取最后归还时间
	 *
	 * @return 最后借出时间
	 */
	long getLastReturn();

	/**
	 * 设置最后归还时间，在成功归还此对象时更新时间
	 *
	 * @param lastReturn 最后归还时间
	 */
	void setLastReturn(final long lastReturn);
}
