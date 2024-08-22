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

package org.dromara.hutool.core.lang.range;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberUtil;

import java.lang.reflect.Type;

/**
 * 片段表示，用于表示文本、集合等数据结构的一个区间。
 * @param <T> 数字类型，用于表示位置index
 *
 * @author looly
 * @since 5.5.3
 */
public interface Segment<T extends Number> {

	/**
	 * 获取起始位置
	 *
	 * @return 起始位置
	 */
	T getBeginIndex();

	/**
	 * 获取结束位置
	 *
	 * @return 结束位置
	 */
	T getEndIndex();

	/**
	 * 片段长度，默认计算方法为abs({@link #getEndIndex()} - {@link #getEndIndex()})
	 *
	 * @return 片段长度
	 */
	default T length(){
		final T start = Assert.notNull(getBeginIndex(), "Start index must be not null!");
		final T end = Assert.notNull(getEndIndex(), "End index must be not null!");
		return Convert.convert((Type) start.getClass(), NumberUtil.sub(end, start).abs());
	}
}
