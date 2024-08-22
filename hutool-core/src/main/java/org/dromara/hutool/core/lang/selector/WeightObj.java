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

package org.dromara.hutool.core.lang.selector;

import java.util.Objects;

/**
 * 带有权重的对象包装
 *
 * @author looly
 *
 * @param <T> 对象类型
 */
public class WeightObj<T> {
	/** 对象 */
	protected T obj;
	/** 权重 */
	protected final int weight;

	/**
	 * 构造
	 *
	 * @param obj 对象
	 * @param weight 权重
	 */
	public WeightObj(final T obj, final int weight) {
		this.obj = obj;
		this.weight = weight;
	}

	/**
	 * 获取对象
	 *
	 * @return 对象
	 */
	public T getObj() {
		return obj;
	}

	/**
	 * 设置对象
	 *
	 * @param obj 对象
	 */
	public void setObj(final T obj) {
		this.obj = obj;
	}

	/**
	 * 获取权重
	 *
	 * @return 权重
	 */
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final WeightObj<?> weightObj = (WeightObj<?>) o;
		return weight == weightObj.weight && Objects.equals(obj, weightObj.obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(obj, weight);
	}
}
